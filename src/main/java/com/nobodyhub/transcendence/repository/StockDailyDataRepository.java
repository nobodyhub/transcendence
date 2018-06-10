package com.nobodyhub.transcendence.repository;

import com.nobodyhub.transcendence.repository.abstr.table.TableRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Table to store stock daily data
 * <p>
 * - Table:         stock_daily_data
 * - Row key:       stock code
 * - Column key:    date
 * - Column value:  close price
 *
 * @author yan_h
 * @since 2018/6/10
 */
@Repository
public class StockDailyDataRepository extends TableRepository<LocalDate> {
    public StockDailyDataRepository() {
        super("stock_daily_data");
    }

    /**
     * the column key is the date in yyyyMMdd format with a prefix <code>d</code>
     *
     * @param date
     * @return
     */
    @Override
    protected String getColumnName(LocalDate date) {
        return "d" + date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    @Override
    protected LocalDate parseColumnName(String columnName) {
        return LocalDate.parse(columnName.substring(1),
                DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
