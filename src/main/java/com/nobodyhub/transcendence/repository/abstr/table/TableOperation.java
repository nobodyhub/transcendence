package com.nobodyhub.transcendence.repository.abstr.table;

import com.datastax.driver.core.ResultSet;

import java.math.BigDecimal;

/**
 * @author yan_h
 * @since 2018/6/10
 */
public interface TableOperation {
    void createTable();

    void addColumn(String column);

    void update(String rowKey, String column, BigDecimal data);

    ResultSet find(String columns);
}
