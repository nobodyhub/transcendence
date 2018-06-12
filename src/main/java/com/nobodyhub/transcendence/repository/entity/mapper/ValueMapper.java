package com.nobodyhub.transcendence.repository.entity.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
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
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String to(Object value) {
        Class cls = value.getClass();
        if (String.class == cls) {
            return (String) value;
        } else if (BigDecimal.class == cls) {
            return ((BigDecimal) value).toPlainString();
        } else if (LocalDate.class == cls) {
            return ((LocalDate) value).format(dateFormatter);
        } else {
            try {
                return objectMapper.writeValueAsString(value);
            } catch (IOException e) {
                throw new ValueMapperFailException(e);
            }
        }

    }

    @SuppressWarnings("unchecked")
    public static <T> T from(String value, Class<T> cls) {
        if (String.class == cls) {
            return (T) value;
        } else if (BigDecimal.class == cls) {
            return (T) new BigDecimal(value);
        } else if (LocalDate.class == cls) {
            return (T) LocalDate.parse(value, dateFormatter);
        } else {
            try {
                return objectMapper.readValue(value, cls);
            } catch (IOException e) {
                throw new ValueMapperFailException(e);
            }
        }
    }
}

class ValueMapperFailException extends RuntimeException {
    ValueMapperFailException(Throwable t) {
        super(t);
    }
}
