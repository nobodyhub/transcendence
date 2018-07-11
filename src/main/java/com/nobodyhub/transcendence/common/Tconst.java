package com.nobodyhub.transcendence.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Common constants
 *
 * @author yan_h
 * @since 2018/6/11
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Tconst {
    /**
     * Date formatter
     */
    public static final DateTimeFormatter dateFormatter
            = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * start of stock market in China
     */
    public static final LocalDate CN_STOCK_START = LocalDate.of(1990, 12, 19);
}
