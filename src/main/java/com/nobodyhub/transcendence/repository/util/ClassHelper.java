package com.nobodyhub.transcendence.repository.util;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author yan_h
 * @since 2018/6/15
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClassHelper {
    /**
     * Get all fields of given class,
     * includes private/protected/publice and those inherited from super class
     *
     * @param cls target class
     * @return
     */
    public static List<Field> getFields(Class cls) {
        Class clazz = cls;
        List<Field> fields = Lists.newArrayList();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                fields.add(field);
            }
            clazz = clazz.getSuperclass();
        }
        return fields;
    }
}
