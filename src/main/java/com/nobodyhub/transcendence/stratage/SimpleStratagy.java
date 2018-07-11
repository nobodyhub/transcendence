package com.nobodyhub.transcendence.stratage;

import com.nobodyhub.transcendence.repository.model.StockIndexInfo;
import com.nobodyhub.transcendence.repository.model.StockIndexSet;
import com.nobodyhub.transcendence.repository.rowdata.RowDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.List;

import static com.nobodyhub.transcendence.common.Tconst.CN_STOCK_START;

/**
 * 一个简单的交易策略
 * 如果五日均线由下而上穿过20日均线, 为买点
 * 如果五日均线由上而下穿过20日均线, 为卖点
 *
 * @author yan_h
 * @since 2018/7/10
 */
@Component
public class SimpleStratagy extends Stratagy {
    @Autowired
    private RowDataRepository repository;

    @Override
    public StratagyResult analyze(LocalDate date, String id) {
        StratagyResult result = StratagyResult.of(date);
        StockIndexInfo indexInfo = fetchIndex(date, id, 20);
        List<StockIndexSet> indexSets = indexInfo.getIndexList();
        BigDecimal intersect = intersect(indexSets);
        StockIndexSet lastIndex = indexSets.get(0);
        BigDecimal avg5Diff = intersect.subtract(lastIndex.getMa5());
        BigDecimal avg5DiffAbs = avg5Diff.abs();
        BigDecimal totalDiffAbs = lastIndex.getMa5().subtract(lastIndex.getMa20()).abs();
        result.setLastClose(lastIndex.getClose());
        if (totalDiffAbs.compareTo(BigDecimal.ZERO) != 0) {
            //上一交易日的收盘价5/20日均线不等
            if (avg5Diff.compareTo(BigDecimal.ZERO) > 0 &&
                    avg5DiffAbs.divide(totalDiffAbs, MathContext.DECIMAL32)
                            .compareTo(new BigDecimal("0.1")) >= 0) {
                //5日均线由下至上穿过, 且波动大于上个交易日5/20均线差价的10%
                result.setBuyPrice(intersect);
            } else if (avg5Diff.compareTo(BigDecimal.ZERO) < 0 &&
                    avg5DiffAbs.divide(totalDiffAbs, MathContext.DECIMAL32)
                            .compareTo(new BigDecimal("0.1")) >= 0) {
                //5日均线由上至下穿过, 且波动大于上个交易日5/20均线差价的10%
                result.setSellPrice(intersect);
            }
        }
        return result;
    }

    /**
     * calculate the intersect of avg5 and avg20
     * <p>
     * for the intersect, Y, which satifies:
     * (X3 + X2 + X1 + X0 + Y) / 5 = (X18 + X17 + X16 + ... + X2 + X1 + X0 + Y) / 20
     *
     * @return
     */
    protected BigDecimal intersect(List<StockIndexSet> indexSets) {
        return (indexSets.get(18).getClose()
                .add(indexSets.get(17).getClose())
                .add(indexSets.get(16).getClose())
                .add(indexSets.get(15).getClose())
                .add(indexSets.get(14).getClose())
                .add(indexSets.get(13).getClose())
                .add(indexSets.get(12).getClose())
                .add(indexSets.get(11).getClose())
                .add(indexSets.get(10).getClose())
                .add(indexSets.get(9).getClose())
                .add(indexSets.get(8).getClose())
                .add(indexSets.get(7).getClose())
                .add(indexSets.get(6).getClose())
                .add(indexSets.get(5).getClose())
                .add(indexSets.get(4).getClose())
                .subtract(new BigDecimal("3").multiply(indexSets.get(3).getClose()))
                .subtract(new BigDecimal("3").multiply(indexSets.get(2).getClose()))
                .subtract(new BigDecimal("3").multiply(indexSets.get(1).getClose()))
                .subtract(new BigDecimal("3").multiply(indexSets.get(0).getClose())))
                .divide(new BigDecimal("3", MathContext.DECIMAL32));
    }


    /**
     * Fetch at least {@code nDays}
     *
     * @param date  analyze date
     * @param id    stock id
     * @param nDays minimun number of days of valid data to fetch
     * @return IndexInfo contains data for at least nDays
     */
    protected StockIndexInfo fetchIndex(LocalDate date, String id, int nDays) {
        final int batchSize = 10;
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
