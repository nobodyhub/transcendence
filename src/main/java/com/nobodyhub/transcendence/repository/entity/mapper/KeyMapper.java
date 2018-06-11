package com.nobodyhub.transcendence.repository.entity.mapper;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

/**
 * Map column key from actual type to String and vice versa
 *
 * @author yan_h
 * @since 2018/6/10
 */
@NoArgsConstructor(access = PRIVATE)
public final class KeyMapper {

    public static String to(Object value) {
        String strVal = ValueMapper.to(value);
        Class cls = value.getClass();
        if (BigDecimal.class == cls) {
            strVal = "dec" + strVal;
        } else if (LocalDate.class == cls) {
            strVal = "date" + strVal;
        }
        return strVal;
    }

    public static <T> T from(String value, Class<T> cls) {
        if (BigDecimal.class == cls) {
            return ValueMapper.from(value.substring(3), cls);
        } else if (LocalDate.class == cls) {
            return ValueMapper.from(value.substring(4), cls);
        }
        return ValueMapper.from(value, cls);
    }
}
