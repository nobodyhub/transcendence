package com.nobodyhub.transcendence.repository.rowdata;

import com.nobodyhub.transcendence.repository.SpringTest;
import com.nobodyhub.transcendence.repository.model.StockBasicInfo;
import com.nobodyhub.transcendence.repository.model.StockIndexInfo;
import com.nobodyhub.transcendence.repository.model.StockIndexSet;
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
 * @since 2018/6/15
 */
public class RowDataRepositoryTest extends SpringTest {


    @Autowired
    private RowDataRepository repository;

    @Before
    public void setUp() {
        repository.createTable(StockBasicInfo.class);
        repository.createTable(StockIndexInfo.class);
    }

    @Test
    public void testUpdateAndQuery() {
        //StockBasicInfo
        StockBasicInfo updateStockBasicInfo = new StockBasicInfo();
        updateStockBasicInfo.setId("sh000001");

        List<StockBasicInfo> stockBasicResults = repository.query(updateStockBasicInfo);
        assertEquals(0, stockBasicResults.size());
        updateStockBasicInfo.setName("上证指数");
        repository.update(updateStockBasicInfo);
        stockBasicResults = repository.query(updateStockBasicInfo);
        assertEquals(stockBasicResults.get(0), updateStockBasicInfo);

        //StockIndexInfo
        StockIndexInfo updateStockIndexInfo = new StockIndexInfo();
        updateStockIndexInfo.setId("sh000001");

        List<StockIndexInfo> stcokIndexResults = repository.query(updateStockIndexInfo);
        assertEquals(0, stcokIndexResults.size());
        StockIndexSet indexSet = StockIndexSet.of(LocalDate.of(2018, 6, 15));
        indexSet.setOpen(new BigDecimal("100"));
        updateStockIndexInfo.addPriceIndex(indexSet);
        repository.update(updateStockIndexInfo);
        stcokIndexResults = repository.query(updateStockIndexInfo);
        assertEquals(stcokIndexResults.get(0), updateStockIndexInfo);
        indexSet.setVolume(new BigDecimal("213412.34"));
        repository.update(updateStockIndexInfo);
        stcokIndexResults = repository.query(updateStockIndexInfo);
        assertEquals(stcokIndexResults.get(0), updateStockIndexInfo);
    }


    @After
    public void tearDown() {
        repository.dropTable(StockBasicInfo.class);
        repository.dropTable(StockIndexInfo.class);
    }
}