package com.lh.learn.commons.datastream;

import java.lang.reflect.Array;

import static java.lang.reflect.Array.newInstance;

/**
 * 数据消费方
 * 
 * @author liuhai Create at: 2018/7/6
 */
public class DataConsumer<T> {
    private DataBlocking<T> dataBlocking;
    private T current;
    private Class clazz;
    private int pos = 0;

    public DataConsumer(DataBlocking<T> dataBlocking, Class clazz) {
        this.dataBlocking = dataBlocking;
        this.clazz = clazz;
    }

    public T getBytes(int size) throws InterruptedException {

        if (size <= 0) {
            return (T)newInstance(clazz, 0);
        }
        if (current == null && dataBlocking.getBlockingQueue().isEmpty() && dataBlocking.isWriteEnd()) {
            return null;
        }
        int cnt = 0;
        Object array = Array.newInstance(clazz, size);

        if (current == null) {
            current = dataBlocking.getBlockingQueue().take();
        }

        int len = Array.getLength(current);

        while (pos + size > len) {
            System.arraycopy(current, pos, array, cnt, len - pos);
            size = size - (len - pos);
            cnt += len - pos;
            pos = 0;
            if (dataBlocking.getBlockingQueue().isEmpty() && dataBlocking.isWriteEnd()) {
                Object res = Array.newInstance(clazz, size);
                System.arraycopy(array, 0, res, 0, cnt);
                return (T)res;
            }
            current = dataBlocking.getBlockingQueue().take();
            len = Array.getLength(current);
        }

        if (size != 0) {
            System.arraycopy(current, pos, array, cnt, size);
            pos = pos + size;
        }

        return (T)array;
    }

}
