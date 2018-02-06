package com.lh.learn.commons.cache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 本地文件使用缓存写入,写入具有严格的先后顺序
 *
 * @author liuhai
 */
public class WriteLocalCache implements Cache {
    /**
     * 容量，超过这个容量将会执行 flush 方法，默认 1M
     */
    private int capacity = 1024 * 1024;

    /**
     * 字符编码,默认使用UTF-8
     */
    private Charset charset = Charset.defaultCharset();

    /**
     * 完整的文件名
     */
    private File file;
    /**
     * 字节缓存流
     */
    private ByteArrayOutputStream outputStream;

    public WriteLocalCache(String fileName) {
        this.file = new File(fileName);
        outputStream = new ByteArrayOutputStream(capacity);
    }

    public WriteLocalCache(String fileName, int capacity) {
        this.file = new File(fileName);
        this.capacity = capacity;
        outputStream = new ByteArrayOutputStream(capacity);
    }

    public WriteLocalCache(String fileName, int capacity, Charset charset) {
        this.file = new File(fileName);
        this.capacity = capacity;
        this.charset = charset;
        outputStream = new ByteArrayOutputStream(capacity);
    }

    public WriteLocalCache(String fileName, Charset charset) {
        this.file = new File(fileName);
        this.charset = charset;
        outputStream = new ByteArrayOutputStream(capacity);
    }

    @Override
    public synchronized void flush() throws IOException {
        if(outputStream.size()>0){
            realWrite(outputStream.toByteArray());
        }
    }

    private synchronized void realWrite(byte[] bytes) throws IOException {
        if (!file.exists()) {
            File file1 = file.getParentFile();
            if (!file1.exists()) {
                file1.mkdirs();
            }
            file.createNewFile();
        }
        try (FileOutputStream writer = new FileOutputStream(file, true)) {
            writer.write(bytes);
            writer.flush();
        }
    }

    @Override
    public void write(String s) throws IOException {
        write(s.getBytes(charset));
    }

    @Override
    public synchronized void write(byte[] bytes) throws IOException {
        if (bytes == null || bytes.length == 0) {
            return;
        }

        if (outputStream.size() + bytes.length > capacity) {
            flush();
            if (bytes.length >= capacity) {
                realWrite(bytes);
            } else {
                outputStream.write(bytes);
            }
        }else{
            outputStream.write(bytes);
        }
    }

    @Override
    public void reset() {
        outputStream.reset();
    }

    public int size() {
        return outputStream.size();
    }

    public int getCapacity() {
        return capacity;
    }

    public Charset getCharset() {
        return charset;
    }

    public File getFile() {
        return file;
    }
}
