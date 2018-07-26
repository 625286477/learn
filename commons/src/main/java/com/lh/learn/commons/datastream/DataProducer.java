package com.lh.learn.commons.datastream;

/**
 * 数据生产方
 *
 * @author liuhai Create at: 2018/7/6
 */
public class DataProducer<T> {
    private DataBlocking<T> dataBlocking;

    public DataProducer(DataBlocking<T> dataBlocking) {
        this.dataBlocking = dataBlocking;
    }

    public boolean add(T t) {
        return dataBlocking.getBlockingQueue().add(t);
    }

    public void setWriteEnd() {
        dataBlocking.setWriteEnd(true);
    }
}
