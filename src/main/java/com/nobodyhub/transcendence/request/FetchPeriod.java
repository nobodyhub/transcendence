package com.nobodyhub.transcendence.request;

import lombok.Getter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Iterator;

/**
 * Data fetch period
 * start from {@link this#start}(inclusive) to {@link this#end} (inclusive)
 *
 * @author yan_h
 * @since 2018/6/29
 */
public class FetchPeriod implements Iterator<FetchPeriod> {
    /**
     * start of stock market in China
     */
    protected static final LocalDate CN_STOCK_START = LocalDate.of(1990, 12, 19);

    /**
     * start of fetch period
     */
    @Getter
    private final LocalDate start;
    /**
     * end of fetch period
     */
    @Getter
    private final LocalDate end;
    /**
     * temporary end of iterator period
     */
    private LocalDate tempEnd;

    public FetchPeriod(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
        this.tempEnd = end;
    }


    /**
     * Get fetch period based on the given fetch size
     *
     * @param fetchSize
     * @return
     */
    public static FetchPeriod of(FetchSize fetchSize) {
        LocalDate end = LocalDate.now();
        LocalDate start = LocalDate.now();
        switch (fetchSize) {
            case ALL: {
                start = CN_STOCK_START;
                break;
            }
            case YEAR: {
                start = end.minusYears(1);
                break;
            }
            case MONTH: {
                start = end.minusMonths(1);
                break;
            }
            case WEEK: {
                start = end.minusWeeks(1);
                break;
            }
            case DAY: {
                start = end.minusDays(1);
                break;
            }
            default: {
                //do nothing
            }
        }
        return new FetchPeriod(start, end);
    }

    public static FetchPeriod of(LocalDate start, LocalDate end) {
        return new FetchPeriod(start, end);
    }

    @Override
    public boolean hasNext() {
        return !tempEnd.isBefore(start);
    }

    /**
     * return fetch perid year by year
     *
     * @return
     */
    @Override
    public FetchPeriod next() {
        LocalDate periodStart = tempEnd.minusYears(1);
        if (periodStart.isBefore(start)) {
            periodStart = start;
        }
        FetchPeriod newPeriod = FetchPeriod.of(periodStart, tempEnd);
        tempEnd = periodStart.minusDays(1);
        return newPeriod;
    }

    public void reset() {
        this.tempEnd = end;
    }

    public long getStartMilli() {
        return this.start
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

    public long getEndMilli() {
        return this.end
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }
}