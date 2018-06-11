package com.nobodyhub.transcendence.repository.entity;

import com.nobodyhub.transcendence.repository.annotation.Id;

/**
 * Abstract class of entity in Cassandra DB
 *
 * @author yan_h
 * @since 2018/6/11
 */
public abstract class Entity {
    /**
     * Row key
     */
    @Id
    private String id;
}
