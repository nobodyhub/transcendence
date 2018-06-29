package com.nobodyhub.transcendence.request;

/**
 * The total size to be fetched from data source
 *
 * @author yan_h
 * @since 2018/6/29
 */
public enum FetchSize {
    /**
     * All data since data start
     */
    ALL,
    /**
     * Data of past year
     */
    YEAR,
    /**
     * Data of past month
     */
    MONTH,
    /**
     * Data of past week
     */
    WEEK,
    /**
     * Data of past date
     */
    DAY
}
