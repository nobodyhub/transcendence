package com.nobodyhub.transcendence.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author yan_h
 * @since 2018/6/15
 */
@Component
public class HttpClient {
    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * a map maintains cookies for each domain
     */
    private ConcurrentMap<String, Set<Cookie>> cookies = Maps.newConcurrentMap();
    private OkHttpClient client = new OkHttpClient.Builder()
            .cookieJar(new CookieJar() {
                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    Map<String, Set<Cookie>> map = Maps.newHashMap();
                    map.put(url.topPrivateDomain(), Sets.newHashSet(cookies));
                    HttpClient.this.cookies.putAll(map);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    HttpClient.this.cookies.putIfAbsent(url.topPrivateDomain(), Sets.newHashSet());
                    return Lists.newArrayList(
                            HttpClient.this.cookies.get(url.topPrivateDomain())
                    );
                }
            })
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    public <T> T execute(Request req, Class<T> cls) throws IOException, InterruptedException {
        logger.info("Sending Request: {}", req);
        Response resp = null;
        try {
            resp = newCall(req);
        } catch (SocketTimeoutException e) {
            logger.error(e.getMessage());
            //sleep for 10s when timeout happens
            Thread.sleep(10000);
        }
        return objectMapper.readValue(resp.body().string(), cls);
    }

    /**
     * send request but ignore response
     * (to refresh the cookies)
     *
     * @param req
     * @return
     * @throws IOException
     */
    public void execute(Request req) throws IOException, InterruptedException {
        logger.info("Sending Request(response ignored): {}", req);
        Response resp = null;
        try {
            resp = newCall(req);
        } finally {
            if (resp != null) {
                resp.close();
            }
        }
    }

    protected Response newCall(Request req) throws IOException, InterruptedException {
        Response resp = null;
        try {
            resp = client.newCall(req).execute();
        } catch (SocketTimeoutException e) {
            logger.error(e.getMessage());
            //sleep for 10s when timeout happens
            Thread.sleep(10000);
        }
        return resp;
    }

}
