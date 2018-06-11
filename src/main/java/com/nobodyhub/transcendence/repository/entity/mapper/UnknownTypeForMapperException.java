package com.nobodyhub.transcendence.repository.entity.mapper;

import lombok.Getter;

/**
 * @author yan_h
 * @since 2018/6/11
 */
public class UnknownTypeForMapperException extends RuntimeException {
    @Getter
    private final Class cls;

    UnknownTypeForMapperException(Class cls) {
        super(String.format("Unknown Type: [ %s ] to map from/to String!",
                cls.getName()));
        this.cls = cls;
    }
}
