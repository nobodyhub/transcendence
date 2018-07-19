package com.nobodyhub.transcendence.fetcher.xueqiu.v5;

import com.google.common.collect.Lists;
import com.nobodyhub.transcendence.SpringTest;
import com.nobodyhub.transcendence.repository.model.StockBasicInfo;
import com.nobodyhub.transcendence.repository.model.StockIndexInfo;
import com.nobodyhub.transcendence.repository.rowdata.RowDataRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * @author yan_h
 * @since 2018/6/15
 */
public class XueqiuDataSourceTest extends SpringTest {
    @Autowired
    private XueqiuDataSource dataSource;
    @Autowired
    private RowDataRepository repository;

    @Before
    public void setUp() {
        repository.createTable(StockBasicInfo.class);
        repository.createTable(StockIndexInfo.class);
    }

    @Test
    public void test() throws IOException, InterruptedException {
        dataSource.persistBasicInfo(Lists.newArrayList());
        dataSource.persistIndexInfo(
                Lists.newArrayList("SZ300750"));
    }

    @After
    public void tearDown() {
        repository.dropTable(StockBasicInfo.class);
        repository.dropTable(StockIndexInfo.class);
    }

}