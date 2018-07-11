package com.nobodyhub.transcendence.request.xueqiu;

import com.datastax.driver.core.Cluster;
import com.google.common.collect.Lists;
import com.nobodyhub.transcendence.ApplicationConfig;
import com.nobodyhub.transcendence.repository.CassandraConfig;
import com.nobodyhub.transcendence.repository.model.StockBasicInfo;
import com.nobodyhub.transcendence.repository.rowdata.RowDataRepository;
import com.nobodyhub.transcendence.request.xueqiu.v5.XueqiuDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.util.stream.Collectors;

/**
 * @author yan_h
 * @since 2018/6/17
 */
@SpringBootApplication
@Import({CassandraConfig.class,
        ApplicationConfig.class})
public class XueqiuApiFetcher implements CommandLineRunner {
    @Autowired
    private XueqiuDataSource dataSource;

    @Autowired
    private RowDataRepository repository;

    @Autowired
    private Cluster cluster;

    public static void main(String[] args) {
        SpringApplication.run(XueqiuApiFetcher.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        dataSource.persistBasicInfo(Lists.newArrayList());
        dataSource.persistIndexInfo(
                repository.query(new StockBasicInfo()).stream()
                        .map(StockBasicInfo::getId)
                        .collect(Collectors.toList()));
        cluster.close();
    }
}
