package com.nobodyhub.transcendence.analyzer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nobodyhub.transcendence.repository.model.StockBasicInfo;
import com.nobodyhub.transcendence.repository.model.StockIndexInfo;
import com.nobodyhub.transcendence.repository.model.StockIndexSet;
import com.nobodyhub.transcendence.repository.rowdata.RowDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author yan_h
 * @since 2018/7/11
 */
@Component
public class StratageVerifier {
    @Autowired
    private StratagyExecutor stratagyExecutor;

    @Autowired
    private RowDataRepository repository;

    @Autowired
    private IndexFetcher fetcher;

    Map<String, StockIndexInfo> indexInfoCache = Maps.newHashMap();

    /**
     * verify the given stratagy using data before <code>endDate</code>
     *
     * @param stratagy
     * @param endDate
     * @return
     */
    public BigDecimal verify(Stratagy stratagy, LocalDate endDate) {
        List<StockBasicInfo> stockBasicInfos = repository.query(new StockBasicInfo());
        int offset = 0;
        //1. get a list of strategyresult for each stratagy
        List<StratagyResult> results = Lists.newArrayList();
        while (!endDate.minusDays(offset).isBefore(fetcher.getStartDate())) {
            final LocalDate curDate = endDate.minusDays(offset);
            stockBasicInfos.forEach(stockBasicInfo -> {
                StockIndexInfo stockIndexInfo = getIndexInfo(curDate,
                        stockBasicInfo.getId(),
                        stratagy.nIndexRequired());
                results.add(stratagy.analyze(stockIndexInfo, curDate));
            });
            offset++;
        }
        //2. based on the sell/buy point and get the profits
        //TODO
        return null;
    }

    public StockIndexInfo getIndexInfo(LocalDate date, String id, int nDays) {
        StockIndexInfo stockIndexInfo = indexInfoCache.get(id);
        if (stockIndexInfo == null) {
            stockIndexInfo = fetcher.fetch(date, id, nDays);
            indexInfoCache.put(id, stockIndexInfo);
        } else {
            List<StockIndexSet> stockIndexSets = stockIndexInfo.getIndexList(date);
            if (stockIndexSets.size() < nDays) {
                StockIndexInfo addition = fetcher.fetch(date, id, nDays);
                stockIndexInfo.merge(Lists.newArrayList(addition));
            }
        }
        return indexInfoCache.get(id);
    }
}
