package com.nobodyhub.transcendence.stratage;

import com.nobodyhub.transcendence.repository.model.StockIndexInfo;
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
     * @param stockIndexInfo the stock index info to be analyzed
     * @param date           the date before which data will be analyzed
     * @return analysis result
     */
    public abstract StratagyResult analyze(StockIndexInfo stockIndexInfo,
                                           LocalDate date);

    /**
     * Register the {@link this} into {@link StratagyExecutor}
     */
    @PostConstruct
    public void setup() {
        stratagyExecutor.register(this);
    }
}
