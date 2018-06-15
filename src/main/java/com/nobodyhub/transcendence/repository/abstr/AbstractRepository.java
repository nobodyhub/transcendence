package com.nobodyhub.transcendence.repository.abstr;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.nobodyhub.transcendence.model.abstr.Entity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author yan_h
 * @since 2018/6/10
 */
public abstract class AbstractRepository {
    @Autowired
    protected Session session;
    @Autowired
    protected Cluster cluster;

    protected abstract void createTable(Class<Entity> entityCls);

    protected abstract void truncateTable(Class<Entity> entityCls);

    protected abstract void update(Entity entity);

    protected abstract void query(Entity entity);

    protected void addColumns(String tableName, Set<String> columnNames) {
        columnNames.forEach(
                (columnName) -> addColumn(tableName, columnName)
        );
    }


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
    private String addColumnCql(String table, String column) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(" ALTER TABLE %s ", table));
        sb.append(String.format(" ADD %s decimal ", column));
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
        sb.append(String.format(" CREATE TABLE IF NOT EXISTS %s ( ", table));
        sb.append(String.format(" %s text PRIMARY KEY ", idColumnName));
        sb.append(" ) ");
        return sb.toString();
    }

    /**
     * CQL to truncate table
     *
     * @param table table name
     * @return
     */
    protected String truncateTableCql(String table) {
        return String.format(" TRUNCATE %s ", table);
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
        sb.append(String.format(" UPDATE %s  ", cfName));
        List<String> assignments = Lists.newArrayList();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            assignments.add(String.format(" SET %s='%s' ",
                    entry.getKey(), entry.getValue()));
        }
        sb.append(Joiner.on(", ").join(assignments));
        sb.append(String.format(" WHERE %s='%s'", rowKeyName, rowKey));
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
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(" SELECT %s ", Joiner.on(", ").join(columnNames)));
        sb.append(String.format(" FROM %s ", cfName));
        sb.append(String.format(" WHERE %s = '%s' ", rowKeyName, rowKey));
        return sb.toString();
    }
}
