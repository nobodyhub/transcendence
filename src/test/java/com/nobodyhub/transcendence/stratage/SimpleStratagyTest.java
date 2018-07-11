package com.nobodyhub.transcendence.stratage;

import com.google.common.collect.Lists;
import com.nobodyhub.transcendence.SpringTest;
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
import java.util.Map;

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

    @Before
    public void setUp() {
        //create table
        repository.createTable(StockIndexInfo.class);

    }

    @Test
    public void testAnalyze() {
        StockIndexInfo info = new StockIndexInfo();
        info.setId("SH600519");
        for (int count = 1; count <= 30; count++) {
            StockIndexSet indexSet = StockIndexSet.of(today.minusDays(count));
            indexSet.setClose(new BigDecimal(count));
            indexSet.setMa5(new BigDecimal("3"));
            indexSet.setMa20(new BigDecimal("10"));
            info.addPriceIndex(indexSet);
        }
        repository.update(info);
        StratagyResult result = simpleStratagy.analyze(today, "SH600519");
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

    @Test
    public void testFetchIndex() {
        //insert 60 records includes nulls
        StockIndexInfo info = new StockIndexInfo();
        info.setId("SH600519");
        for (int count = 1; count <= 60; count++) {
            if (count % 5 == 0) {
                //insert null value on that day
                continue;
            }
            StockIndexSet indexSet = StockIndexSet.of(today.minusDays(count));
            indexSet.setClose(new BigDecimal(count));
            info.addPriceIndex(indexSet);
        }
        repository.update(info);

        StockIndexInfo stockIndexInfo = simpleStratagy.fetchIndex(today, "SH600519", 10);
        Map<LocalDate, StockIndexSet> indexSets = stockIndexInfo.getIndices();
        assertEquals(16, indexSets.size());
        assertEquals(true, indexSets.containsKey(LocalDate.of(2018, 7, 10)));
        assertEquals(true, indexSets.containsKey(LocalDate.of(2018, 7, 9)));
        assertEquals(true, indexSets.containsKey(LocalDate.of(2018, 7, 8)));
        assertEquals(true, indexSets.containsKey(LocalDate.of(2018, 7, 7)));
        assertEquals(true, indexSets.containsKey(LocalDate.of(2018, 7, 5)));
        assertEquals(true, indexSets.containsKey(LocalDate.of(2018, 7, 4)));
        assertEquals(true, indexSets.containsKey(LocalDate.of(2018, 7, 3)));
        assertEquals(true, indexSets.containsKey(LocalDate.of(2018, 7, 2)));
        assertEquals(true, indexSets.containsKey(LocalDate.of(2018, 6, 30)));
        assertEquals(true, indexSets.containsKey(LocalDate.of(2018, 6, 29)));
        assertEquals(true, indexSets.containsKey(LocalDate.of(2018, 6, 28)));
        assertEquals(true, indexSets.containsKey(LocalDate.of(2018, 6, 27)));
        assertEquals(true, indexSets.containsKey(LocalDate.of(2018, 6, 25)));
        assertEquals(true, indexSets.containsKey(LocalDate.of(2018, 6, 24)));
        assertEquals(true, indexSets.containsKey(LocalDate.of(2018, 6, 23)));
        assertEquals(true, indexSets.containsKey(LocalDate.of(2018, 6, 22)));
    }

    /**
     * <b>Note:</b> this test takes more than 1 hour
     */
    @Test
    public void testFetchIndex2() {
        StockIndexInfo stockIndexInfo = simpleStratagy.fetchIndex(today, "SH600520", 10);
        assertEquals(0, stockIndexInfo.getIndices().size());
    }

    @After
    public void tearDown() {
        repository.dropTable(StockIndexInfo.class);
    }
}