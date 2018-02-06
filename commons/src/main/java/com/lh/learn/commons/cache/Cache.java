package com.lh.learn.commons.cache;

import java.io.IOException;

/**
 * must write something to descript this file.
 *
 * @author liuhai
 */
public interface Cache {

    void flush() throws IOException;
    void write(String s) throws IOException;
    void write(byte[] bytes) throws IOException;
    void reset();
}
