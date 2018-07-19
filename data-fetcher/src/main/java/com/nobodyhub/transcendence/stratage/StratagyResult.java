package com.nobodyhub.transcendence.stratage;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * The result of stratage analysis
 *
 * @author yan_h
 * @since 2018/7/10
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class StratagyResult {
    private final LocalDate date;
    private BigDecimal sellPrice;
    private BigDecimal buyPrice;
    private BigDecimal lastClose;

    public static StratagyResult of(LocalDate date) {
        return new StratagyResult(date);
    }

    /**
     * Whether is a valid analysis result
     *
     * @return
     */
    public boolean isValid() {
        return !(buyPrice == null && sellPrice == null);
    }

}
