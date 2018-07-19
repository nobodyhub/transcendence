package com.nobodyhub.transcendence.repository.model.abstr;

/**
 * @param <T> entity type
 * @author yan_h
 * @since 2018/6/17
 */
public interface EntityBuilder<T> {
    /**
     * Build a new entity
     *
     * @return
     */
    T build();
}
