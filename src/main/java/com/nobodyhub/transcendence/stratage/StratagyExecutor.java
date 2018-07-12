package com.nobodyhub.transcendence.stratage;

import com.google.common.collect.Lists;
import com.nobodyhub.transcendence.repository.model.StockIndexInfo;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * @author yan_h
 * @since 2018/7/11
 */
@Component
public class StratagyExecutor {
    private List<Stratagy> stratagies = Lists.newArrayList();

    public void register(Stratagy stratagy) {
        stratagies.add(stratagy);
    }

    /**
     * Start the execution of the stratagy
     * No further changes on {@link this#stratagies} is accepted
     *
     * @param stockIndexInfo
     * @param date
     * @return
     */
    public List<StratagyResult> start(StockIndexInfo stockIndexInfo,
                                      LocalDate date) {
        List<Stratagy> stratagyList = Collections.unmodifiableList(stratagies);
        List<StratagyResult> stratagyResults = Lists.newArrayList();
        for (Stratagy stratagy : stratagyList) {
            stratagyResults.add(stratagy.analyze(stockIndexInfo, date));
        }
        return stratagyResults;
    }
}
