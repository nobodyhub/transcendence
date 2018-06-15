package com.nobodyhub.transcendence.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Index related to the Stock price
 *
 * @author yan_h
 * @since 2018/6/15
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__(@JsonCreator))
@Data
@EqualsAndHashCode
public class StockIndexSet {
    public static StockIndexSet of(LocalDate date) {
        StockIndexSet indexSet = new StockIndexSet();
        indexSet.setDate(date);
        return indexSet;
    }

    /**
     * 交易日期
     */
    @Setter(AccessLevel.PRIVATE)
    private LocalDate date;
    /**
     * 成交量
     */
    private BigDecimal volume;
    /**
     * 开盘价
     */
    private BigDecimal open;
    /**
     * 最高价
     */
    private BigDecimal high;
    /**
     * 最低价
     */
    private BigDecimal low;
    /**
     * 收盘价
     */
    private BigDecimal close;
    /**
     * 涨跌额
     */
    private BigDecimal chg;
    /**
     * 涨跌幅
     */
    private BigDecimal percent;
    /**
     * 换手率
     */
    private BigDecimal turnoverrate;
    /**
     * 5日均线
     */
    private BigDecimal ma5;
    /**
     * 10日均线
     */
    private BigDecimal ma10;
    /**
     * 20日均线
     */
    private BigDecimal ma20;
    /**
     * 30日均线
     */
    private BigDecimal ma30;

    /**
     * MACD (12,26,9), 平滑移动平均线
     *
     * @see <a href="https://en.wikipedia.org/wiki/MACD"></a>
     */
    private BigDecimal dea;
    private BigDecimal dif;
    private BigDecimal macd;
    /**
     * BOLL(20,2), 布林线指标
     *
     * @see <a href="https://en.wikipedia.org/wiki/Bollinger_Bands"></a>
     */
    private BigDecimal ub;
    private BigDecimal lb;
    private BigDecimal mid;
    /**
     * KDJ(9,3,3), 随机指标
     *
     * @see <a href="https://en.wikipedia.org/wiki/Stochastic_oscillator"></a>
     */
    private BigDecimal kdjk;
    private BigDecimal kdjd;
    private BigDecimal kdjj;
    /**
     * RSI(6,12,24), 相对强弱指标
     *
     * @see <a href="https://en.wikipedia.org/wiki/Relative_strength_index"></a>
     */
    private BigDecimal rsi1;
    private BigDecimal rsi2;
    private BigDecimal rsi3;
    /**
     * WR(6,20), 威廉指标
     *
     * @see <a href="https://en.wikipedia.org/wiki/Williams_%25R"></a>
     */
    private BigDecimal wr6;
    private BigDecimal wr10;
    /**
     * BIAS(6,12,24), 乖离率
     */
    private BigDecimal bias1;
    private BigDecimal bias2;
    private BigDecimal bias3;
    /**
     * CCI(14), 顺势指标
     */
    private BigDecimal cci;
    /**
     * PSY(12,6), 心理线
     */
    private BigDecimal psy;
    private BigDecimal psym;
}
