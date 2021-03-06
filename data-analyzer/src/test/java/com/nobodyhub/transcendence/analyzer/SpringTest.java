package com.nobodyhub.transcendence.analyzer;

import com.nobodyhub.transcendence.repository.CassandraConfig;
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
        AnalyzerConfig.class
})
@TestPropertySource(locations = "classpath:test.properties")
public abstract class SpringTest {
}