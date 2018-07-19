package com.nobodyhub.transcendence.fetcher.xueqiu.v5;

import com.google.common.collect.Lists;
import com.nobodyhub.transcendence.repository.model.StockBasicInfo;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author yan_h
 * @since 2018/6/15
 */
@Data
public class StockList {
    private Map<String, Integer> count;
    private String success;
    private List<Map<String, String>> stocks;

    public List<StockBasicInfo> toStockBasicInfo() {
        List<StockBasicInfo> infoList = Lists.newArrayList();
        for (Map<String, String> stock : stocks) {
            StockBasicInfo info = new StockBasicInfo();
            info.setId(stock.get("symbol"));
            info.setName(stock.get("name"));
            infoList.add(info);
        }
        return infoList;
    }
}
