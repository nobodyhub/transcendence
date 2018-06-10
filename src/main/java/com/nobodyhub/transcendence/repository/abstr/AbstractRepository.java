package com.nobodyhub.transcendence.repository.abstr;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author yan_h
 * @since 2018/6/10
 */
@Repository
public abstract class AbstractRepository {
    @Autowired
    protected Session session;
    @Autowired
    protected Cluster cluster;
}
