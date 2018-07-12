package com.nobodyhub.transcendence.stratage;

import com.nobodyhub.transcendence.SpringTest;
import com.nobodyhub.transcendence.repository.model.StockIndexInfo;
import com.nobodyhub.transcendence.repository.model.StockIndexSet;
import com.nobodyhub.transcendence.repository.rowdata.RowDataRepository;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author yan_h
 * @since 2018/7/11
 */
public class StratagyExecutorTest extends SpringTest {
    @Autowired
    private RowDataRepository repository;

    @Autowired
    private StratagyExecutor stratagyExecutor;

    private LocalDate today = LocalDate.of(2018, 7, 11);

    @Before
    public void setUp() {
        //create table
        repository.createTable(StockIndexInfo.class);

    }

    @Test
    public void testStart() {
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
        List<StratagyResult> results = stratagyExecutor.start(info, today);
        assertEquals(1, results.size());
        TestCase.assertEquals(new BigDecimal("50"), results.get(0).getBuyPrice());
        TestCase.assertEquals(null, results.get(0).getSellPrice());
        TestCase.assertEquals(new BigDecimal("1"), results.get(0).getLastClose());
    }

    @After
    public void tearDown() {
        repository.dropTable(StockIndexInfo.class);
    }
}