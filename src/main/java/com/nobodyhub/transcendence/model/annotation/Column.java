package com.nobodyhub.transcendence.model.annotation;

import com.nobodyhub.transcendence.model.abstr.Entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate field as column of {@link Entity}
 *
 * @author yan_h
 * @since 2018/6/10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
}
