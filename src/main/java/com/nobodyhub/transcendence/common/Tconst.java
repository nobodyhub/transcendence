package com.nobodyhub.transcendence.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
}
