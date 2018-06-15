package com.nobodyhub.transcendence.repository.abstr;

import com.nobodyhub.transcendence.repository.config.CassandraConfig;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author yan_h
 * @since 2018/6/10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CassandraConfig.class)
@TestPropertySource(locations = "classpath:test.properties")
public abstract class AbstractRepositoryTest<T extends AbstractRepository> {
    @Autowired
    protected T repository;

}