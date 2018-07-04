package com.lh.learn.experiment.concurrent;

/**
 * 描述类的作用
 *
 * @author liuhai
 *         Create at: 2018/5/8
 */
class Lock {
    private boolean isLocked = false;

    synchronized void lock() throws InterruptedException {
        // 这种称为自旋锁
        while (isLocked) {
            wait();
        }
        isLocked = true;
    }

    synchronized void unlock() {
        isLocked = false;
        notify();
    }
}


