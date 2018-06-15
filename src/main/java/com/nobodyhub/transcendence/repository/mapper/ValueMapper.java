package com.nobodyhub.transcendence.repository.mapper;

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
    /**
     * Convert object to its string representive as column value
     *
     * @param value
     * @return
     */
    public static String to(Object value) {
        Class cls = value.getClass();
        if (String.class == cls) {
            return (String) value;
        } else if (BigDecimal.class == cls) {
            return ((BigDecimal) value).toPlainString();
        } else if (LocalDate.class == cls) {
            return ((LocalDate) value).format(dateFormatter);
        }
        throw new ValueMapperFailException(cls);

    }

    /**
     * Convert string to given type as column value
     *
     * @param value
     * @param cls
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T from(String value, Class<T> cls) {
        if (String.class == cls) {
            return (T) value;
        } else if (BigDecimal.class == cls) {
            return (T) new BigDecimal(value);
        } else if (LocalDate.class == cls) {
            return (T) LocalDate.parse(value, dateFormatter);
        }
        throw new ValueMapperFailException(cls);
    }
}

class ValueMapperFailException extends RuntimeException {
    ValueMapperFailException(Class cls) {
        super(String.format("Can not handle type: [ %s ] to/from String!",
                cls.getName()));
    }
}
