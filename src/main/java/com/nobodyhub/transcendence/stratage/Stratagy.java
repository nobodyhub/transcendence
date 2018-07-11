package com.nobodyhub.transcendence.stratage;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

/**
 * Common interface for stratage
 *
 * @author yan_h
 * @since 2018/7/10
 */
public abstract class Stratagy {
    /**
     * Stratagy Executor
     */
    @Autowired
    protected StratagyExecutor stratagyExecutor;

    /**
     * Execute stratage analysiss
     *
     * @param date the date before which the transaction closed
     * @param id   stock id, e.g., SH600519
     * @return analysis result
     */
    public abstract StratagyResult analyze(LocalDate date, String id);

    /**
     * Register the {@link this} into {@link StratagyExecutor}
     */
    @PostConstruct
    public void setup() {
        stratagyExecutor.register(this);
    }
}
