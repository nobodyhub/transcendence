package com.nobodyhub.transcendence.request.xueqiu.v5;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nobodyhub.transcendence.repository.model.StockIndexInfo;
import lombok.Data;

/**
 * @author yan_h
 * @since 2018/6/15
 */
@Data
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