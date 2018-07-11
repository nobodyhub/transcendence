package com.nobodyhub.transcendence.request;

import com.nobodyhub.transcendence.common.Tconst;
import org.junit.Test;

import java.time.LocalDate;

import static junit.framework.TestCase.assertEquals;

/**
 * @author yan_h
 * @since 2018/6/29
 */
public class FetchPeriodTest {
    @Test
    public void testOf() {
        FetchPeriod fetchPeriod = FetchPeriod.of(FetchSize.ALL);
        assertEquals(Tconst.CN_STOCK_START, fetchPeriod.getStart());
        assertEquals(LocalDate.now(), fetchPeriod.getEnd());

        fetchPeriod = FetchPeriod.of(FetchSize.YEAR);
        assertEquals(LocalDate.now().minusYears(1), fetchPeriod.getStart());
        assertEquals(LocalDate.now(), fetchPeriod.getEnd());

        fetchPeriod = FetchPeriod.of(FetchSize.MONTH);
        assertEquals(LocalDate.now().minusMonths(1), fetchPeriod.getStart());
        assertEquals(LocalDate.now(), fetchPeriod.getEnd());

        fetchPeriod = FetchPeriod.of(FetchSize.WEEK);
        assertEquals(LocalDate.now().minusWeeks(1), fetchPeriod.getStart());
        assertEquals(LocalDate.now(), fetchPeriod.getEnd());

        fetchPeriod = FetchPeriod.of(FetchSize.DAY);
        assertEquals(LocalDate.now().minusDays(1), fetchPeriod.getStart());
        assertEquals(LocalDate.now(), fetchPeriod.getEnd());
    }

    @Test
    public void testIterator() {
        FetchPeriod fetchPeriod = FetchPeriod.of(LocalDate.of(2014, 7, 1),
                LocalDate.of(2018, 6, 28));

        assertEquals(true, fetchPeriod.hasNext());
        FetchPeriod curPeriod = fetchPeriod.next();
        assertEquals(LocalDate.of(2017, 6, 28), curPeriod.getStart());
        assertEquals(LocalDate.of(2018, 6, 28), curPeriod.getEnd());

        assertEquals(true, fetchPeriod.hasNext());
        curPeriod = fetchPeriod.next();
        assertEquals(LocalDate.of(2016, 6, 27), curPeriod.getStart());
        assertEquals(LocalDate.of(2017, 6, 27), curPeriod.getEnd());

        assertEquals(true, fetchPeriod.hasNext());
        curPeriod = fetchPeriod.next();
        assertEquals(LocalDate.of(2015, 6, 26), curPeriod.getStart());
        assertEquals(LocalDate.of(2016, 6, 26), curPeriod.getEnd());

        assertEquals(true, fetchPeriod.hasNext());
        curPeriod = fetchPeriod.next();
        assertEquals(LocalDate.of(2014, 7, 1), curPeriod.getStart());
        assertEquals(LocalDate.of(2015, 6, 25), curPeriod.getEnd());

        assertEquals(false, fetchPeriod.hasNext());

        fetchPeriod.reset();
        assertEquals(true, fetchPeriod.hasNext());
        curPeriod = fetchPeriod.next();
        assertEquals(LocalDate.of(2017, 6, 28), curPeriod.getStart());
        assertEquals(LocalDate.of(2018, 6, 28), curPeriod.getEnd());
    }

    @Test
    public void testGetMilli() {
        FetchPeriod fetchPeriod = FetchPeriod.of(LocalDate.of(2014, 7, 1),
                LocalDate.of(2018, 6, 28));
        assertEquals(1404144000000L, fetchPeriod.getStartMilli());
        assertEquals(1530115200000L, fetchPeriod.getEndMilli());
    }
}