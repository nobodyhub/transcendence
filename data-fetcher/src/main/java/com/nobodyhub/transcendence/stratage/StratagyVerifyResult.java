package com.nobodyhub.transcendence.stratage;

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
        return profits.stream().reduce(BigDecimal::add).get()
                .subtract(losses.stream().reduce(BigDecimal::add).get());
    }
}
