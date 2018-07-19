package com.nobodyhub.transcendence.fetcher;

import java.io.IOException;
import java.util.List;

/**
 * Interface to describe a data source for stock data
 *
 * @author yan_h
 * @since 2018/6/15
 */
public interface StockDataSource {
    /**
     * Get stock basic info
     *
     * @param stockIds stock ids
     * @return
     * @throws IOException
     */
    void persistBasicInfo(List<String> stockIds) throws IOException, InterruptedException;

    /**
     * Get stock index info
     *
     * @param stockIds stock ids
     * @return
     * @throws IOException
     */
    void persistIndexInfo(List<String> stockIds) throws IOException, InterruptedException;
}
