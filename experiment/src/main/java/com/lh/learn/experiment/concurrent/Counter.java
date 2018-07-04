package com.lh.learn.experiment.concurrent;

/**
 * 线程安全的计数器
 *
 * @author liuhai
 *         Create at: 2018/5/8
 */
public class Counter {
    private Lock lock = new Lock();
    private int count = 0;

    public int inc() {
        try {
            lock.lock();
            return ++count;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
