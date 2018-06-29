package com.nobodyhub.transcendence.request.xueqiu.v5;

import com.google.common.collect.Sets;
import com.nobodyhub.transcendence.repository.model.StockBasicInfo;
import com.nobodyhub.transcendence.repository.rowdata.RowDataRepository;
import com.nobodyhub.transcendence.request.FetchPeriod;
import com.nobodyhub.transcendence.request.FetchSize;
import com.nobodyhub.transcendence.request.HttpClient;
import com.nobodyhub.transcendence.request.StockDataSource;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author yan_h
 * @since 2018/6/15
 */
@Component
public class XueqiuDataSource implements StockDataSource {
    private static final Logger logger = LoggerFactory.getLogger(XueqiuDataSource.class);
    /**
     * Http client to send the request
     */
    @Autowired
    private HttpClient client;

    /**
     * Repository to query/update data
     */
    @Autowired
    private RowDataRepository repository;

    /**
     * Date size to be fetched
     */
    @Value("${fetch.size}")
    private FetchSize fetchSize;

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
            FetchPeriod fetchPeriod = FetchPeriod.of(fetchSize);
            while (fetchPeriod.hasNext()) {
                FetchPeriod curPeriod = fetchPeriod.next();
                Request dataReq = new Request.Builder()
                        .url(new HttpUrl.Builder()
                                .scheme("https")
                                .host("stock.xueqiu.com")
                                .addPathSegments("v5/stock/chart/kline.json")
                                .addQueryParameter("symbol", stock.toUpperCase())
                                .addQueryParameter("begin", String.valueOf(curPeriod.getStartMilli()))
                                .addQueryParameter("end", String.valueOf(curPeriod.getEndMilli()))
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
                    logger.debug("Empty StockDataSet Skipped! {}", stockDataSet);
                    break;
                }
            }
        }
    }
}
