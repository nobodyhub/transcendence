package com.nobodyhub.transcendence.repository.rowdata;

import com.nobodyhub.transcendence.SpringTest;
import com.nobodyhub.transcendence.model.StockBasicInfo;
import com.nobodyhub.transcendence.model.StockIndexInfo;
import com.nobodyhub.transcendence.model.StockIndexSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;

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
        StockBasicInfo resultStockBasicInfo = new StockBasicInfo();
        resultStockBasicInfo.setId("sh000001");

        repository.query(resultStockBasicInfo);
        assertEquals(resultStockBasicInfo, updateStockBasicInfo);
        repository.update(updateStockBasicInfo);
        repository.query(resultStockBasicInfo);
        assertEquals(resultStockBasicInfo, updateStockBasicInfo);
        updateStockBasicInfo.setName("上证指数");
        repository.update(updateStockBasicInfo);
        repository.query(resultStockBasicInfo);
        assertEquals(resultStockBasicInfo, updateStockBasicInfo);

        //StockIndexInfo
        StockIndexInfo updateStockIndexInfo = new StockIndexInfo();
        updateStockIndexInfo.setId("sh000001");
        StockIndexInfo resultStockIndexInfo = new StockIndexInfo();
        resultStockIndexInfo.setId("sh000001");

        repository.query(resultStockIndexInfo);
        assertEquals(resultStockIndexInfo, updateStockIndexInfo);
        repository.update(updateStockIndexInfo);
        repository.query(resultStockIndexInfo);
        assertEquals(resultStockIndexInfo, updateStockIndexInfo);
        StockIndexSet indexSet = StockIndexSet.of(LocalDate.of(2018, 6, 15));
        indexSet.setOpen(new BigDecimal("100"));
        updateStockIndexInfo.addPriceIndex(indexSet);
        resultStockIndexInfo.addPriceIndex(StockIndexSet.of(LocalDate.of(2018, 6, 15)));
        repository.update(updateStockIndexInfo);
        repository.query(resultStockIndexInfo);
        assertEquals(resultStockIndexInfo, updateStockIndexInfo);
        indexSet.setVolume(new BigDecimal("213412.34"));
        repository.update(updateStockIndexInfo);
        repository.query(resultStockIndexInfo);
        assertEquals(resultStockIndexInfo, updateStockIndexInfo);
    }


    @After
    public void tearDown() {
        repository.dropTable(StockBasicInfo.class);
        repository.dropTable(StockIndexInfo.class);
    }
}