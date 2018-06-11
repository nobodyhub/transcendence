package com.nobodyhub.transcendence.repository.annotation;

import com.nobodyhub.transcendence.repository.entity.Entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate class as {@link Entity}
 *
 * @author yan_h
 * @since 2018/6/10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ColumnFamily {
    String value();
}
