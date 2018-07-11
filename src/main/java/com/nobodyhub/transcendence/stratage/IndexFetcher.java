package com.nobodyhub.transcendence.stratage;

import com.nobodyhub.transcendence.repository.model.StockIndexInfo;
import com.nobodyhub.transcendence.repository.model.StockIndexSet;
import com.nobodyhub.transcendence.repository.rowdata.RowDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.nobodyhub.transcendence.common.Tconst.CN_STOCK_START;

/**
 * @author yan_h
 * @since 2018/7/12
 */
@Component
public class IndexFetcher {
    @Autowired
    private RowDataRepository repository;

    @Value("#{T(java.time.LocalDate).parse('${stratagy.fetch.start}')}")
    private LocalDate startDate;

    @Value("${stratagy.fetch.batch}")
    private int batchSize;

    public StockIndexInfo fetch(LocalDate date, String id, int nDays) {
        StockIndexInfo indexInfo = new StockIndexInfo();
        indexInfo.setId(id);
        int nBatch = 1;
        while (indexInfo.getIndices().size() < nDays) {
            int batchOffset = batchSize * (nBatch - 1);
            int batchEnd = batchSize * nBatch;
            StockIndexInfo query = new StockIndexInfo();
            query.setId(id);
            while (batchOffset < batchEnd) {
                query.addPriceIndex(StockIndexSet.of(date.minusDays(batchOffset)));
                batchOffset++;
            }
            indexInfo.merge(repository.query(query));
            nBatch++;
            if (date.minusDays(batchEnd).isBefore(CN_STOCK_START)) {
                break;
            }
        }
        return indexInfo;
    }
}
