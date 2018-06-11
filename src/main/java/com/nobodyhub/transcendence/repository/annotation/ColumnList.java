package com.nobodyhub.transcendence.repository.annotation;

import com.nobodyhub.transcendence.repository.entity.Entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Set;

/**
 * Annotate field of type {@link List} or {@link Set}
 * as column(s) of {@link Entity}
 *
 * @author yan_h
 * @since 2018/6/11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ColumnList {
}
