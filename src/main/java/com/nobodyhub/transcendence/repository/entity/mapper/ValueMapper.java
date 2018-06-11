package com.nobodyhub.transcendence.repository.entity.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.nobodyhub.transcendence.common.Tconst.dateFormatter;

/**
 * Map column value from actual type to String and vice versa
 *
 * @author yan_h
 * @since 2018/6/11
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValueMapper {

    public static String to(Object value) {
        Class cls = value.getClass();
        if (String.class == cls) {
            return (String) value;
        } else if (BigDecimal.class == cls) {
            return ((BigDecimal) value).toPlainString();
        } else if (LocalDate.class == cls) {
            return ((LocalDate) value).format(dateFormatter);
        }
        throw new UnknownTypeForMapperException(cls);
    }

    @SuppressWarnings("unchecked")
    public static <T> T from(String value, Class<T> cls) {
        if (String.class == cls) {
            return (T) value;
        } else if (BigDecimal.class == cls) {
            return (T) new BigDecimal(value);
        } else if (LocalDate.class == cls) {
            return (T) LocalDate.parse(value, dateFormatter);
        }
        throw new UnknownTypeForMapperException(cls);
    }
}
