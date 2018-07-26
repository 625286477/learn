package com.lh.learn.commons.datastream;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author liuhai Create at: 2018/7/6
 */
public class DataBlocking<T> {
    /**
     * 数据分片保存队列
     */
    private LinkedBlockingQueue<T> blockingQueue = new LinkedBlockingQueue<>();

    /**
     * 写入结束标志
     */
    private volatile boolean writeEnd;

    public LinkedBlockingQueue<T> getBlockingQueue() {
        return blockingQueue;
    }

    public void setBlockingQueue(LinkedBlockingQueue<T> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    public boolean isWriteEnd() {
        return writeEnd;
    }

    public void setWriteEnd(boolean writeEnd) {
        this.writeEnd = writeEnd;
    }
}
