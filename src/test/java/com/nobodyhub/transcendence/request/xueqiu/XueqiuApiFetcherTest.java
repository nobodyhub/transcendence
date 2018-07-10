package com.nobodyhub.transcendence.request.xueqiu;

import com.nobodyhub.transcendence.SpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yan_h
 * @since 2018/7/11
 */
public class XueqiuApiFetcherTest extends SpringTest {
    @Autowired
    private XueqiuApiFetcher fetcher;

    @Test
    public void testRun() throws Exception {
        fetcher.run(null);
    }
}