package com.nobodyhub.transcendence.repository.abstr.table;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.google.common.collect.Maps;
import com.nobodyhub.transcendence.repository.abstr.AbstractRepository;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.SortedMap;

/**
 * Base class of table(column family) repository
 * <p>
 * - each row has a unique key, whose name is <code>id</code>
 * - the column key of each column is a string
 * - the column value of each column, except the <code>id</code> column, is decimal
 *
 * @author yan_h
 * @since 2018/6/10
 */
@RequiredArgsConstructor
public abstract class TableRepository<T extends Comparable<? super T>> extends AbstractRepository
        implements TableOperation<T> {
    /**
     * Table name
     */
    protected final String tableName;

    @PostConstruct
    @Override
    public void createTable() {
        this.session.execute(createTableCql(tableName));
    }

    @Override
    public void truncateTable() {
        this.session.execute(truncateTableCql(tableName));
    }

    @Override
    public void addColumn(String column) {
        try {
            this.session.execute(addColumnCql(tableName, column));
        } catch (InvalidQueryException e) {
            //fail if column exist, safely ignored
            //TODO: add logger
        }
    }

    @Override
    public void update(String rowKey, T column, BigDecimal data) {
        String columnName = getColumnName(column);
        this.addColumn(columnName);
        this.session.execute(updateCql(tableName, rowKey, columnName, data));
    }

    @Override
    public SortedMap<T, BigDecimal> find(String rowKey) {
        ResultSet resultSet = this.session.execute(selectCql(tableName, rowKey));
        SortedMap<T, BigDecimal> values = Maps.newTreeMap();
        for (Row row : resultSet) {
            ColumnDefinitions colDefs = row.getColumnDefinitions();
            for (ColumnDefinitions.Definition colDef : colDefs) {
                String colName = colDef.getName();
                if ("id".compareToIgnoreCase(colName) == 0) {
                    continue;
                }
                BigDecimal value = row.getDecimal(colName);
                if (value != null) {
                    values.put(parseColumnName(colName), value);
                }
            }
        }
        return values;
    }

    /**
     * Convert column key type to a string
     * <p>
     * the reverse operation is {@link this#parseColumnName(String)}
     *
     * @param column actual column key
     * @return
     */
    protected abstract String getColumnName(T column);

    /**
     * Convert String to actual column key
     * <p>
     * a reverse operation to {@link this#getColumnName(Comparable)}
     *
     * @param columnName column key in string format
     * @return
     */
    protected abstract T parseColumnName(String columnName);


    /**
     * CQL to create table
     *
     * @param table table name
     * @return
     */
    private String createTableCql(String table) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(" CREATE TABLE IF NOT EXISTS %s ( ", table));
        sb.append(" id text PRIMARY KEY ");
        sb.append(" ) ");
        return sb.toString();
    }

    /**
     * CQL to truncate table
     *
     * @param table table name
     * @return
     */
    private String truncateTableCql(String table) {
        return String.format(" TRUNCATE %s ", table);
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
     * CQL to update data
     *
     * @param table  table name
     * @param rowKey row key
     * @param column column key
     * @param data   column value
     * @return
     */
    private String updateCql(String table, String rowKey, String column, BigDecimal data) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(" UPDATE %s  ", table));
        sb.append(" SET ");
        sb.append(String.format(" %s = %s ", column, data));
        sb.append(String.format(" WHERE id='%s'", rowKey));
        return sb.toString();
    }

    /**
     * CQL to fetch data
     *
     * @param table  table name
     * @param rowKey row key
     * @return
     */
    private String selectCql(String table, String rowKey) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT * ");
        sb.append(String.format(" FROM %s ", table));
        sb.append(String.format(" WHERE id = '%s' ", rowKey));
        return sb.toString();
    }
}
