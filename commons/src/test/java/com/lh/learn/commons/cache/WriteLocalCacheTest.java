package com.lh.learn.commons.cache;

import org.junit.Test;

import java.io.IOException;

/**
 * @author liuhai
 */
public class WriteLocalCacheTest {

    @Test
    public void test() throws IOException {
        WriteLocalCache writeLocalCache = new WriteLocalCache("/Users/liuhai/Desktop/test/WriteLocalCacheTest.txt", 10);
        for (int i = 0; i < 100; i++) {
            writeLocalCache.write("hahahahahahhahhhhhhaa" + i + "\n");
        }
        writeLocalCache.flush();
    }
}