package com.nobodyhub.transcendence.repository;

import com.nobodyhub.transcendence.repository.abstr.AbstractRepositoryTest;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author yan_h
 * @since 2018/6/10
 */
public class StockDailyDataRepositoryTest extends AbstractRepositoryTest<StockDailyDataRepository> {
    @Test
    public void test() {
        repository.update("sh00001",
                LocalDate.of(2018, 6, 1),
                new BigDecimal("60.1"));
        repository.update("sh00001",
                LocalDate.of(2018, 6, 3),
                new BigDecimal("60.3"));

        repository.update("sh00002",
                LocalDate.of(2018, 6, 1),
                new BigDecimal("260.1"));
        repository.update("sh00002",
                LocalDate.of(2018, 6, 2),
                new BigDecimal("260.2"));

        Map<LocalDate, BigDecimal> result1 = repository.find("sh00001");
        assertEquals(2, result1.size());
        Map<LocalDate, BigDecimal> result2 = repository.find("sh00002");
        assertEquals(2, result2.size());
    }
}