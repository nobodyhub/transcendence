package com.nobodyhub.transcendence.repository.abstr;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nobodyhub.transcendence.model.abstr.Entity;
import org.springframework.beans.factory.annotation.Autowired;

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
     * 1. non-null value for @Id field
     * 2. all @Column fields will be fetched
     * 3. for @ColumnMap field, only fetch those whose keys are contained in map of given entity
     *
     * @param entity
     */
    public abstract void query(Entity entity);

    /**
     * Add a set of columns to given table
     *
     * @param tableName
     * @param columnNames
     */
    protected void addColumns(String tableName, Set<String> columnNames) {
        columnNames.forEach(
                (columnName) -> addColumn(tableName, columnName)
        );
    }

    /**
     * add column to table
     * if column exists, will return silently, no exception thrown
     *
     * @param tableName
     * @param columnName
     */
    protected void addColumn(String tableName, String columnName) {
        try {
            this.session.execute(addColumnCql(tableName, columnName));
        } catch (InvalidQueryException e) {
            //fail if column exist, safely ignored
            //TODO: add logger
        }
    }

    /**
     * CQL to add a column
     *
     * @param table  table name
     * @param column column name(key)
     * @return
     */
    protected String addColumnCql(String table, String column) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(" ALTER TABLE \"%s\" ", table));
        sb.append(String.format(" ADD \"%s\" text ", column));
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
        sb.append(String.format(" UPDATE \"%s\"  ", cfName));
        List<String> assignments = Lists.newArrayList();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String value = entry.getValue();
            if (value == null) {
                assignments.add(String.format(" SET \"%s\"=null ",
                        entry.getKey()));
            } else {
                assignments.add(String.format(" SET \"%s\"='%s' ",
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
        sb.append(String.format(" WHERE \"%s\"='%s' ", rowKeyName, rowKey));
        return sb.toString();
    }
}
