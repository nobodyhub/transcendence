package com.nobodyhub.transcendence.repository.abstr;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.Session;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nobodyhub.transcendence.repository.model.abstr.Entity;
import com.nobodyhub.transcendence.repository.model.annotation.ColumnFamily;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Repository for basic db operations
 * <b>Note</b>:
 * the column key and column value are both of string type
 * <p>
 * identifiers, like table name and column name are quoted with <code>"</code>,
 * thus they are case-sensitive.
 *
 * @author yan_h
 * @since 2018/6/10
 */
public abstract class AbstractRepository {
    @Autowired
    protected Session session;
    @Autowired
    protected Cluster cluster;

    /**
     * Create table(if not exist) for given entity
     *
     * @param entityCls
     */
    public abstract void createTable(Class<? extends Entity> entityCls);

    /**
     * Drop table(if exist) for given entity
     *
     * @param entityCls
     */
    public abstract void dropTable(Class<? extends Entity> entityCls);

    /**
     * Update the content given by entity to related table
     *
     * @param entity
     */
    public abstract void update(Entity entity);

    /**
     * Query content for given entity
     * the query entity should have:
     * 1. if @Id field's value is null, query all data. Otherwise, only the row with given row key
     * 2. all @Column fields will be fetched
     * 3. for @ColumnMap field, only fetch those whose keys are contained in map of given entity
     *
     * @param entity
     * @return
     */
    public abstract <T extends Entity<T>> List<T> query(T entity);

    /**
     * Add a set of columns to given table
     *
     * @param tableName
     * @param columnNames
     */
    protected void addColumns(String tableName, Set<String> columnNames) {
        //filter existed columns
        Set<String> nonExist = columnNames.stream().filter(colName -> {
            ColumnMetadata columnMeta = cluster.getMetadata()
                    .getKeyspace(session.getLoggedKeyspace())
                    .getTable(tableName)
                    .getColumn("\"" + colName + "\"");
            return columnMeta == null;
        }).collect(Collectors.toSet());
        // add non-existed columns
        if (!nonExist.isEmpty()) {
            this.session.execute(addColumnsCql(tableName, nonExist));
        }
    }

    /**
     * Create tables for class inherited from {@link Entity}
     * and has annotation {@link ColumnFamily}
     */
    @PostConstruct
    public void createTableIfNotExist() {
        new Reflections(
                new ConfigurationBuilder()
                        .setUrls(
                                ClasspathHelper.forPackage("com.nobodyhub.transcendence"))
                        .setScanners(
                                new SubTypesScanner(),
                                new TypeAnnotationsScanner())

        );
        Reflections reflections = new Reflections(
                "com.nobodyhub.transcendence");
        Set<Class<? extends Entity>> entities = reflections.getSubTypesOf(Entity.class);
        entities.stream()
                .filter(clz -> clz.isAnnotationPresent(ColumnFamily.class))
                .forEach(this::createTable);
    }

    /**
     * CQL to add a column
     *
     * @param table   table name
     * @param columns column names(key)
     * @return
     */
    protected String addColumnsCql(String table, Set<String> columns) {
        Set<String> colCqls = columns.stream().map(column ->
                String.format(" \"%s\" text ", column))
                .collect(Collectors.toSet());
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(" ALTER TABLE \"%s\" ", table));
        sb.append(String.format(" ADD ( %s )", Joiner.on(", ").join(colCqls)));
        return sb.toString();
    }

    /**
     * CQL to create table
     *
     * @param table table name
     * @return
     */
    protected String createTableCql(String table, String idColumnName) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(" CREATE TABLE IF NOT EXISTS \"%s\" ( ", table));
        sb.append(String.format(" \"%s\" text PRIMARY KEY ", idColumnName));
        sb.append(" ) ");
        return sb.toString();
    }

    /**
     * CQL to truncate table
     *
     * @param table table name
     * @return
     */
    protected String dropTableCql(String table) {
        return String.format(" DROP TABLE IF EXISTS \"%s\" ", table);
    }

    /**
     * CQL to update data
     *
     * @param cfName     column family name
     * @param rowKeyName row key name
     * @param rowKey     row key
     * @param values     values contains a map from key to value
     * @return
     */
    protected String updateCql(String cfName,
                               String rowKeyName,
                               String rowKey,
                               Map<String, String> values) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(" UPDATE \"%s\" SET ", cfName));
        List<String> assignments = Lists.newArrayList();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String value = entry.getValue();
            if (value == null) {
                assignments.add(String.format(" \"%s\"=null ",
                        entry.getKey()));
            } else {
                assignments.add(String.format(" \"%s\"='%s' ",
                        entry.getKey(), value));
            }
        }
        sb.append(Joiner.on(", ").join(assignments));
        sb.append(String.format(" WHERE \"%s\"='%s'", rowKeyName, rowKey));
        return sb.toString();
    }

    /**
     * CQL to fetch data
     *
     * @param cfName      table name
     * @param rowKeyName  row key name
     * @param rowKey      row key
     * @param columnNames column names to be selected
     * @return
     */
    protected String selectCql(String cfName,
                               String rowKeyName,
                               String rowKey,
                               Set<String> columnNames) {
        Set<String> cols = Sets.newHashSet(columnNames);
        cols.add(rowKeyName);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(" SELECT %s ", Joiner.on(", ").join(
                cols.stream().map(ele -> "\"" + ele + "\"")
                        .collect(Collectors.toList()))));
        sb.append(String.format(" FROM \"%s\" ", cfName));
        if (rowKey != null) {
            sb.append(String.format(" WHERE \"%s\"='%s' ", rowKeyName, rowKey));
        }
        return sb.toString();
    }
}
