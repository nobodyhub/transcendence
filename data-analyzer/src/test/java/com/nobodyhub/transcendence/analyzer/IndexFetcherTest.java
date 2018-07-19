package com.nobodyhub.transcendence.analyzer;

import com.nobodyhub.transcendence.repository.model.StockIndexInfo;
import com.nobodyhub.transcendence.repository.model.StockIndexSet;
import com.nobodyhub.transcendence.repository.rowdata.RowDataRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

/**
 * @author yan_h
 * @since 2018/7/12
 */
public class IndexFetcherTest extends SpringTest {
    @Autowired
    private IndexFetcher fetcher;

    @Autowired
    private RowDataRepository repository;

    private final LocalDate today = LocalDate.of(2018, 7, 11);

    private final String stockId = "SH600519";

    @Before
    public void setUp() {
        //create table
        repository.createTable(StockIndexInfo.class);

        //insert 60 records includes nulls
        StockIndexInfo info = new StockIndexInfo();
        info.setId(stockId);
        //insert future date
        info.addPriceIndex(StockIndexSet.of(today.plusDays(1)));
        info.addPriceIndex(StockIndexSet.of(today.plusDays(2)));
        info.addPriceIndex(StockIndexSet.of(today.plusDays(3)));
        //insert past data
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
    }

    @After
    public void tearDown() {
        repository.dropTable(StockIndexInfo.class);
    }

    @Test
    public void testFetch() {
        StockIndexInfo stockIndexInfo = fetcher.fetch(today, stockId, 10);
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
     * no data to fetch, will end at ${stratagy.fetch.start}
     */
    @Test
    public void testFetchIndex() {
        StockIndexInfo stockIndexInfo = fetcher.fetch(today, "SH600520", 10);
        assertEquals(0, stockIndexInfo.getIndices().size());
    }

}