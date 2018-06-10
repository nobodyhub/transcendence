package com.nobodyhub.transcendence.repository.abstr.table;

import java.math.BigDecimal;
import java.util.SortedMap;

/**
 * Basic operaions of table(column family)
 *
 * @param <T> the actual key type of column key(name)
 * @author yan_h
 * @since 2018/6/10
 */
public interface TableOperation<T extends Comparable<? super T>> {
    /**
     * Create table
     */
    void createTable();

    /**
     * Truncate table
     */
    void truncateTable();

    /**
     * Add column
     * if column exist, InvalidQueryException will be caught silently
     *
     * @param column name of column
     */
    void addColumn(String column);

    /**
     * Update data
     *
     * @param rowKey row key
     * @param column column key
     * @param data   column value
     */
    void update(String rowKey, T column, BigDecimal data);

    /**
     * Fetch data
     *
     * @param rowKey row key
     * @return a sorted map from actual column key to column value
     */
    SortedMap<T, BigDecimal> find(String rowKey);
}
