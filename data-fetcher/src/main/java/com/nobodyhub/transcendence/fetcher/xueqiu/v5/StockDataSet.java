package com.nobodyhub.transcendence.fetcher.xueqiu.v5;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nobodyhub.transcendence.repository.model.StockIndexInfo;
import lombok.Data;
import lombok.ToString;

/**
 * @author yan_h
 * @since 2018/6/15
 */
@Data
@ToString
public class StockDataSet {
    private StockData data;

    @JsonProperty("error_code")
    private int errorCode;

    @JsonProperty("error_description")
    private String errorDescription;

    public StockIndexInfo toStockIndexInfo() {
        return data.toStockIndexInfo();
    }

    public boolean isValid() {
        return errorCode == 0
                && !data.isEmpty();
    }
}
