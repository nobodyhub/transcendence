package com.nobodyhub.transcendence.repository.mapper;

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

    /**
     * Convert object to its string representive as column key
     * Additional prefix will be added in order to get an valie key name:
     * - BigDecimal: "DEC_", "." will be replaced with "_"
     * - LocalDate: "DATE_"
     *
     * @param value
     * @return
     */
    public static String to(Object value) {
        String strVal = ValueMapper.to(value);
        Class cls = value.getClass();
        if (BigDecimal.class == cls) {
            strVal = "DEC" + strVal;
            strVal = strVal.replaceAll("\\.", "_");
        } else if (LocalDate.class == cls) {
            strVal = "DATE" + strVal;
        }
        return strVal;
    }

    /**
     * Convert string to given type as column key
     *
     * @param value
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T from(String value, Class<T> cls) {
        if (BigDecimal.class == cls) {
            return ValueMapper.from(value.substring(3)
                    .replaceAll("_", "."), cls);
        } else if (LocalDate.class == cls) {
            return ValueMapper.from(value.substring(4), cls);
        }
        return ValueMapper.from(value, cls);
    }
}
