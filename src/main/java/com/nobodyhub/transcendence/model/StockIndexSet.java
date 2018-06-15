package com.nobodyhub.transcendence.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Index related to the Stock price
 *
 * @author yan_h
 * @since 2018/6/15
 */
@Data
public class StockIndexSet {
    /**
     * 交易日期
     */
    LocalDate date;
    /**
     * 成交量
     */
    BigDecimal volume;
    /**
     * 开盘价
     */
    BigDecimal open;
    /**
     * 最高价
     */
    BigDecimal high;
    /**
     * 最低价
     */
    BigDecimal low;
    /**
     * 收盘价
     */
    BigDecimal close;
    /**
     * 涨跌额
     */
    BigDecimal chg;
    /**
     * 涨跌幅
     */
    BigDecimal percent;
    /**
     * 换手率
     */
    BigDecimal turnoverrate;
    /**
     * 5日均线
     */
    BigDecimal ma5;
    /**
     * 10日均线
     */
    BigDecimal ma10;
    /**
     * 20日均线
     */
    BigDecimal ma20;
    /**
     * 30日均线
     */
    BigDecimal ma30;

    /**
     * MACD (12,26,9), 平滑移动平均线
     *
     * @see <a href="https://en.wikipedia.org/wiki/MACD"></a>
     */
    BigDecimal dea;
    BigDecimal dif;
    BigDecimal macd;
    /**
     * BOLL(20,2), 布林线指标
     *
     * @see <a href="https://en.wikipedia.org/wiki/Bollinger_Bands"></a>
     */
    BigDecimal ub;
    BigDecimal lb;
    BigDecimal mid;
    /**
     * KDJ(9,3,3), 随机指标
     *
     * @see <a href="https://en.wikipedia.org/wiki/Stochastic_oscillator"></a>
     */
    BigDecimal kdjk;
    BigDecimal kdjd;
    BigDecimal kdjj;
    /**
     * RSI(6,12,24), 相对强弱指标
     *
     * @see <a href="https://en.wikipedia.org/wiki/Relative_strength_index"></a>
     */
    BigDecimal rsi1;
    BigDecimal rsi2;
    BigDecimal rsi3;
    /**
     * WR(6,20), 威廉指标
     *
     * @see <a href="https://en.wikipedia.org/wiki/Williams_%25R"></a>
     */
    BigDecimal wr6;
    BigDecimal wr10;
    /**
     * BIAS(6,12,24), 乖离率
     */
    BigDecimal bias1;
    BigDecimal bias2;
    BigDecimal bias3;
    /**
     * CCI(14), 顺势指标
     */
    BigDecimal cci;
    /**
     * PSY(12,6), 心理线
     */
    BigDecimal psy;
    BigDecimal psym;
}
