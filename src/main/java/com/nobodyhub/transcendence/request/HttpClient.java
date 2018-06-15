package com.nobodyhub.transcendence.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * @author yan_h
 * @since 2018/6/15
 */
@Component
public class HttpClient {
    private final static ObjectMapper objectMapper = new ObjectMapper();
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
            .build();

    public <T> T execute(Request req, Class<T> cls) throws IOException {
        Response resp = execute(req);
        return objectMapper.readValue(resp.body().string(), cls);
    }

    public Response execute(Request req) throws IOException {
        return client.newCall(req).execute();
    }

}
