package com.nobodyhub.transcendence.repository.model.abstr;

import com.nobodyhub.transcendence.repository.model.annotation.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Abstract class of entity in Cassandra DB
 *
 * @author yan_h
 * @since 2018/6/11
 */
@Data
@EqualsAndHashCode
public abstract class Entity<E> implements EntityBuilder<E> {
    /**
     * Row key
     */
    @Id
    protected String id;
}
