package com.nobodyhub.transcendence.repository.model;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static junit.framework.TestCase.assertEquals;


/**
 * @author yan_h
 * @since 2018/7/12
 */
public class StockIndexInfoTest {
    @Test
    public void testMerge() {
        StockIndexSet set1 = StockIndexSet.of(LocalDate.of(2018, 7, 1));
        StockIndexSet set2 = StockIndexSet.of(LocalDate.of(2018, 7, 2));
        StockIndexSet set3 = StockIndexSet.of(LocalDate.of(2018, 7, 3));
        StockIndexSet set3_1 = StockIndexSet.of(LocalDate.of(2018, 7, 3));
        set3_1.setMa20(new BigDecimal("20"));
        StockIndexSet set4 = StockIndexSet.of(LocalDate.of(2018, 7, 4));
        StockIndexSet set5 = StockIndexSet.of(LocalDate.of(2018, 7, 5));

        StockIndexInfo indexInfo = new StockIndexInfo();
        indexInfo.addPriceIndex(set1);
        indexInfo.addPriceIndex(set2);
        indexInfo.addPriceIndex(set3);
        StockIndexInfo toMerge = new StockIndexInfo();
        toMerge.addPriceIndex(set3_1);
        toMerge.addPriceIndex(set4);
        toMerge.addPriceIndex(set5);
        indexInfo.merge(Lists.newArrayList(toMerge));
        assertEquals(5, indexInfo.getIndices().size());
        assertEquals(true, indexInfo.getIndices().containsKey(LocalDate.of(2018, 7, 1)));
        assertEquals(true, indexInfo.getIndices().containsKey(LocalDate.of(2018, 7, 2)));
        assertEquals(true, indexInfo.getIndices().containsKey(LocalDate.of(2018, 7, 3)));
        assertEquals(true, indexInfo.getIndices().containsKey(LocalDate.of(2018, 7, 4)));
        assertEquals(true, indexInfo.getIndices().containsKey(LocalDate.of(2018, 7, 5)));
        assertEquals(new BigDecimal("20"), indexInfo.getIndices().get(LocalDate.of(2018, 7, 3)).getMa20());
    }

    @Test
    public void getIndexListTest() {
        StockIndexInfo indexInfo = new StockIndexInfo();
        indexInfo.addPriceIndex(StockIndexSet.of(LocalDate.of(2018, 7, 1)));
        indexInfo.addPriceIndex(StockIndexSet.of(LocalDate.of(2018, 7, 2)));
        indexInfo.addPriceIndex(StockIndexSet.of(LocalDate.of(2018, 7, 3)));
        indexInfo.addPriceIndex(StockIndexSet.of(LocalDate.of(2018, 7, 4)));
        indexInfo.addPriceIndex(StockIndexSet.of(LocalDate.of(2018, 7, 5)));
        List<StockIndexSet> indexSets = indexInfo.getIndexList(LocalDate.of(2018, 7, 3));
        assertEquals(2, indexSets.size());
    }
}