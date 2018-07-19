package com.nobodyhub.transcendence.analyzer;

import com.google.common.collect.Lists;
import com.nobodyhub.transcendence.repository.model.StockIndexInfo;
import com.nobodyhub.transcendence.repository.model.StockIndexSet;
import com.nobodyhub.transcendence.repository.rowdata.RowDataRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * @author yan_h
 * @since 2018/7/11
 */
public class SimpleStratagyTest extends SpringTest {
    @Autowired
    private SimpleStratagy simpleStratagy;

    @Autowired
    private RowDataRepository repository;

    private LocalDate today = LocalDate.of(2018, 7, 11);

    private StockIndexInfo info;

    @Before
    public void setUp() {
        //create table
        repository.createTable(StockIndexInfo.class);

        //insert 60 records includes nulls
        info = new StockIndexInfo();
        info.setId("SH600519");
        //insert past data
        for (int count = 1; count <= 60; count++) {
            StockIndexSet indexSet = StockIndexSet.of(today.minusDays(count));
            indexSet.setClose(new BigDecimal(count));
            indexSet.setMa5(new BigDecimal("3"));
            indexSet.setMa20(new BigDecimal("10"));
            info.addPriceIndex(indexSet);
        }
        repository.update(info);
    }

    @Test
    public void testAnalyze() {
        StratagyResult result = simpleStratagy.analyze(info, today);
        assertEquals(new BigDecimal("50"), result.getBuyPrice());
        assertEquals(null, result.getSellPrice());
        assertEquals(new BigDecimal("1"), result.getLastClose());
    }

    @Test
    public void testIntersect() {
        List<StockIndexSet> indexSets = Lists.newArrayList();
        for (int count = 1; count <= 30; count++) {
            StockIndexSet indexSet = StockIndexSet.of(today.minusDays(count));
            indexSet.setClose(new BigDecimal(count));
            indexSets.add(indexSet);
        }
        assertEquals(new BigDecimal("50"), simpleStratagy.intersect(indexSets));
    }

    @After
    public void tearDown() {
        repository.dropTable(StockIndexInfo.class);
    }
}