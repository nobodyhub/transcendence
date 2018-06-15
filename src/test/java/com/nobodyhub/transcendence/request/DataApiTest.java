package com.nobodyhub.transcendence.request;

import com.google.common.collect.Lists;
import okhttp3.*;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author yan_h
 * @since 2018/6/10
 */
public class DataApiTest {
    private List<Cookie> cookies = Lists.newArrayList();

    @Test
    public void test() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        DataApiTest.this.cookies.addAll(cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        return DataApiTest.this.cookies;
                    }
                })
                .build();

        //to refresh the tokens in cookies
        Request pageReq = new Request.Builder()
                .url("https://xueqiu.com/S/SH600519")
                .build();
        client.newCall(pageReq).execute();
        Request dataReq = new Request.Builder()
                .url("https://stock.xueqiu.com/v5/stock/chart/kline.json?symbol=SH600519&begin=998841600000&end=1528636620307&period=day&type=before&indicator=kline")
                .build();
        Response dataResp = client.newCall(dataReq).execute();
        dataResp.body().string();
    }

}