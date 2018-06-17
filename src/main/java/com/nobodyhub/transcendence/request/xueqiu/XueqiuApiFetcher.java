package com.nobodyhub.transcendence.request.xueqiu;

import com.google.common.collect.Lists;
import com.nobodyhub.transcendence.repository.CassandraConfig;
import com.nobodyhub.transcendence.repository.model.StockBasicInfo;
import com.nobodyhub.transcendence.repository.rowdata.RowDataRepository;
import com.nobodyhub.transcendence.request.RequestConfig;
import com.nobodyhub.transcendence.request.xueqiu.v5.XueqiuDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.stream.Collectors;

/**
 * @author yan_h
 * @since 2018/6/17
 */
@SpringBootApplication
@Import({CassandraConfig.class,
        RequestConfig.class})
public class XueqiuApiFetcher {
    @Autowired
    private XueqiuDataSource dataSource;

    @Autowired
    private RowDataRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(XueqiuApiFetcher.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            dataSource.persistBasicInfo(Lists.newArrayList());
            dataSource.persistIndexInfo(
                    repository.query(new StockBasicInfo()).stream()
                            .map(StockBasicInfo::getId)
                            .collect(Collectors.toList()));
        };
    }
}
