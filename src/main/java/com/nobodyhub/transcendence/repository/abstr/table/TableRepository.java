package com.nobodyhub.transcendence.repository.abstr.table;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.nobodyhub.transcendence.repository.abstr.AbstractRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

/**
 * @author yan_h
 * @since 2018/6/10
 */
@RequiredArgsConstructor
public class TableRepository extends AbstractRepository implements TableOperation {
    protected final String tableName;

    @Override
    public void createTable() {
        this.session.execute(createTableCql(tableName));
    }

    @Override
    public void addColumn(String column) {
        try {
            this.session.execute(addColumnCql(tableName, column));
        } catch (InvalidQueryException e) {
            //ignore
        }
    }

    @Override
    public void update(String rowKey, String column, BigDecimal data) {
        this.session.execute(updateCql(tableName, rowKey, column, data));
    }

    @Override
    public ResultSet find(String columns) {
        return this.session.execute(selectCql(
                tableName,
                columns));
    }

    private String createTableCql(String table) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(" CREATE TABLE IF NOT EXISTS %s ( ", table));
        sb.append(" id text PRIMARY KEY ");
        sb.append(" ) ");
        return sb.toString();
    }

    private String addColumnCql(String table, String column) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(" ALTER TABLE %s ", table));
        sb.append(String.format(" ADD %s decimal ", column));
        return sb.toString();
    }

    private String updateCql(String table, String rowKey, String column, BigDecimal data) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(" UPDATE %s  ", table));
        sb.append(" SET ");
        sb.append(String.format(" %s = %s ", column, data));
        sb.append(String.format(" WHERE id='%s'", rowKey));
        return sb.toString();
    }

    private String selectCql(String table, String column) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(" SELECT %s ", column));
        sb.append(String.format(" FROM %s ", table));
        return sb.toString();
    }
}
