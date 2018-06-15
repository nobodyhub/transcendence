package com.nobodyhub.transcendence.request;

import com.nobodyhub.transcendence.repository.model.StockBasicInfo;
import com.nobodyhub.transcendence.repository.model.StockIndexInfo;

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
     * @param stocks stock ids
     * @return
     * @throws IOException
     */
    List<StockBasicInfo> getBasicInfo(List<String> stocks) throws IOException;

    /**
     * Get stock index info
     *
     * @param stocks stock ids
     * @return
     * @throws IOException
     */
    List<StockIndexInfo> getIndexInfo(List<String> stocks) throws IOException;
}
