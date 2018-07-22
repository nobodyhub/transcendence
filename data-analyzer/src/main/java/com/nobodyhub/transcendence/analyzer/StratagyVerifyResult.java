package com.nobodyhub.transcendence.analyzer;

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yan_h
 * @since 2018/7/11
 */
public class StratagyVerifyResult {
    /**
     * Each profit
     */
    private List<BigDecimal> profits = Lists.newArrayList();
    /**
     * Each loss
     */
    private List<BigDecimal> losses = Lists.newArrayList();

    /**
     * total profit or loss if negat
     *
     * @return
     */
    public BigDecimal getTotalProfit() {
        BigDecimal profit = profits.stream().reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        BigDecimal loss = losses.stream().reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        return profit.subtract(loss);
    }
}
