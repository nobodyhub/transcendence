package com.nobodyhub.transcendence.repository.annotation;

import com.nobodyhub.transcendence.repository.entity.Entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * Annotate field of type {@link Map}
 * as column(s) of {@link Entity}
 *
 * @author yan_h
 * @since 2018/6/10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ColumnMap {
}