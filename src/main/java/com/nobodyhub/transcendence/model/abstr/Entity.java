package com.nobodyhub.transcendence.model.abstr;

import com.nobodyhub.transcendence.model.annotation.Id;

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
