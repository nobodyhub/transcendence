package com.nobodyhub.transcendence.repository.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.nobodyhub.transcendence.repository.mapper.serializer.LocalDateDeserializer;
import com.nobodyhub.transcendence.repository.mapper.serializer.LocalDateSerializer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.time.LocalDate;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Map column value from actual type to String and vice versa
 *
 * @author yan_h
 * @since 2018/6/11
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValueMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDate.class, new LocalDateSerializer())
                .addDeserializer(LocalDate.class, new LocalDateDeserializer());
        objectMapper.registerModule(module);
    }

    /**
     * Convert object to its string representive as column value
     *
     * @param value
     * @return
     */
    public static String to(Object value) {
        if (value.getClass() == String.class) {
            return (String) value;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ValueMapperFailException(value.getClass(), e.getMessage());
        }
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
        if (cls == String.class) {
            return (T) value;
        }
        try {
            return objectMapper.readValue(value, cls);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ValueMapperFailException(cls, e.getMessage());
        }
    }
}

class ValueMapperFailException extends RuntimeException {
    ValueMapperFailException(Class cls, String msg) {
        super(String.format(
                "Error Happends when handling type: [ %s ] to/from String!%n %s",
                cls.getName(), msg));
    }
}
