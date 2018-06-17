package com.nobodyhub.transcendence.request.xueqiu.v5;

import com.google.common.collect.Sets;
import com.nobodyhub.transcendence.repository.model.StockBasicInfo;
import com.nobodyhub.transcendence.repository.model.StockIndexInfo;
import com.nobodyhub.transcendence.repository.rowdata.RowDataRepository;
import com.nobodyhub.transcendence.request.HttpClient;
import com.nobodyhub.transcendence.request.StockDataSource;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

/**
 * @author yan_h
 * @since 2018/6/15
 */
@Component
public class XueqiuDataSource implements StockDataSource {
    @Autowired
    private HttpClient client;

    @Autowired
    private RowDataRepository repository;

    @Override
    public void persistBasicInfo(List<String> stocks) throws IOException {
        //refresh cookies
        Request pageReq = new Request.Builder()
                .url(new HttpUrl.Builder()
                        .scheme("https")
                        .host("xueqiu.com")
                        .addPathSegment("hq")
                        .build())
                .build();
        client.execute(pageReq);
        //request stock list
        //use set to remove potential duplications in last page
        Set<StockBasicInfo> stockSet = Sets.newHashSet();
        int stockCount = 0;
        int page = 1;
        while (true) {
            Request dataReq = new Request.Builder()
                    .url(new HttpUrl.Builder()
                            .scheme("https")
                            .host("xueqiu.com")
                            .addPathSegments("stock/cata/stocklist.json")
                            .addQueryParameter("type", "11,12")
                            .addQueryParameter("size", "90")
                            .addQueryParameter("page", String.valueOf(page++))
                            .addQueryParameter("order", "desc")
                            .addQueryParameter("orderby", "percent")
                            .build())
                    .build();
            StockList stockList = client.execute(dataReq, StockList.class);
            stockSet.addAll(stockList.toStockBasicInfo());
            stockCount = stockCount + stockList.getStocks().size();
            if (stockCount >= stockList.getCount().get("count")) {
                break;
            }
        }
        //persist
        stockSet.forEach(info -> repository.update(info));
    }

    @Override
    public void persistIndexInfo(List<String> stocks) throws IOException {
        Set<StockIndexInfo> infoSet = Sets.newHashSet();
        for (String stock : stocks) {
            //refresh cookies
            Request pageReq = new Request.Builder()
                    .url(new HttpUrl.Builder()
                            .scheme("https")
                            .host("xueqiu.com")
                            .addPathSegments("S")
                            .addPathSegment(stock.toUpperCase())
                            .build())
                    .build();
            client.execute(pageReq);
            Request dataReq = new Request.Builder()
                    .url(new HttpUrl.Builder()
                            .scheme("https")
                            .host("stock.xueqiu.com")
                            .addPathSegments("v5/stock/chart/kline.json")
                            .addQueryParameter("symbol", stock.toUpperCase())
                            .addQueryParameter("begin", "0")
                            .addQueryParameter("end",
                                    String.valueOf(LocalDate.now()
                                            .atStartOfDay()
                                            .atZone(ZoneId.systemDefault())
                                            .toInstant().toEpochMilli()))
                            .addQueryParameter("period", "day")
                            .addQueryParameter("type", "before")
                            .addQueryParameter("indicator",
                                    "kline,ma,macd,kdj,boll,rsi,wr,bias,cci,psy")
                            .build())
                    .build();
            StockDataSet stockDataSet = client.execute(dataReq, StockDataSet.class);
            //filter those which might not be stock or has error
            if (stockDataSet.isValid()) {
                //persist
                repository.update(stockDataSet.toStockIndexInfo());
            } else {
                //TODO: add logger
            }
        }
    }
}
