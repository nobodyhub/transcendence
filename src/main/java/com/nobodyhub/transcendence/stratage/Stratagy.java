package com.nobodyhub.transcendence.stratage;

import java.time.LocalDate;

/**
 * Common interface for stratage
 *
 * @author yan_h
 * @since 2018/7/10
 */
public interface Stratagy {
    /**
     * Execute stratage analysiss
     *
     * @param date the date before which the transaction closed
     * @param id   stock id, e.g., SH600519
     * @return analysis result
     */
    StratagyResult analyze(LocalDate date, String id);
}
