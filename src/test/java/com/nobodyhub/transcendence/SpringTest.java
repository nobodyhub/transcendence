package com.nobodyhub.transcendence;

import com.nobodyhub.transcendence.repository.CassandraConfig;
import com.nobodyhub.transcendence.request.RequestConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author yan_h
 * @since 2018/6/10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        CassandraConfig.class,
        RequestConfig.class
})
@TestPropertySource(locations = "classpath:test.properties")
public abstract class SpringTest {
}