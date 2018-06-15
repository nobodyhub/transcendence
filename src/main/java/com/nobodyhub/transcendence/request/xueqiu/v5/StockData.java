package com.nobodyhub.transcendence.request.xueqiu.v5;

import com.nobodyhub.transcendence.repository.model.StockIndexInfo;
import com.nobodyhub.transcendence.repository.model.StockIndexSet;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

/**
 * @author yan_h
 * @since 2018/6/15
 */
@Data
public class StockData {
    private String symbol;
    /**
     * 0- timestamp
     * 1 - volume
     * 2 - open
     * 3 - high
     * 4 - low
     * 5 - close
     * 6 - chg
     * 7 - percent
     * 8 - turnoverrate
     * 9  - ma5
     * 10 - ma10
     * 11 - ma20
     * 12 - ma30
     * 13 - dea
     * 14 - dif
     * 15 - macd
     * 16 - ub
     * 17 - lb
     * 18 - ma20
     * 19 - kdjk
     * 20 - kdjd
     * 21 - kdjj
     * 22 - rsi1
     * 23 - rsi2
     * 24 - rsi3
     * 25 - wr6
     * 26 - wr10
     * 27 - bias1
     * 28 - bias2
     * 29 - bias3
     * 30 - cci
     * 31 - psy
     * 32 - psyma
     */
    private List<String> column;
    private List<List<BigDecimal>> item;

    public StockIndexInfo toStockIndexInfo() {
        StockIndexInfo indexInfo = new StockIndexInfo();
        indexInfo.setId(symbol);
        for (List<BigDecimal> data : item) {
            StockIndexSet index = StockIndexSet.of(
                    Instant.ofEpochMilli(data.get(0).longValue())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate());
            index.setVolume(data.get(1));
            index.setOpen(data.get(2));
            index.setHigh(data.get(3));
            index.setLow(data.get(4));
            index.setClose(data.get(5));
            index.setChg(data.get(6));
            index.setPercent(data.get(7));
            index.setTurnoverrate(data.get(8));
            index.setMa5(data.get(9));
            index.setMa10(data.get(10));
            index.setMa20(data.get(11));
            index.setMa30(data.get(12));
            index.setDea(data.get(13));
            index.setDif(data.get(14));
            index.setMacd(data.get(15));
            index.setUb(data.get(16));
            index.setLb(data.get(17));
            index.setMb(data.get(18));
            index.setKdjk(data.get(19));
            index.setKdjd(data.get(20));
            index.setKdjj(data.get(21));
            index.setRsi1(data.get(22));
            index.setRsi2(data.get(23));
            index.setRsi3(data.get(24));
            index.setWr6(data.get(25));
            index.setWr10(data.get(26));
            index.setBias1(data.get(27));
            index.setBias2(data.get(28));
            index.setBias3(data.get(29));
            index.setCci(data.get(30));
            index.setPsy(data.get(31));
            index.setPsyma(data.get(32));
            indexInfo.addPriceIndex(index);
        }
        return indexInfo;
    }
}
