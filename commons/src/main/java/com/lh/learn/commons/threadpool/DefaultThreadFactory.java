package com.lh.learn.commons.threadpool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * must write something to descript this file.
 *
 * @author liuhai
 */
public class DefaultThreadFactory implements ThreadFactory {

    private final String namePrefix;
    private final ThreadGroup group;
    private final AtomicLong count;

    DefaultThreadFactory(final String namePrefix, final ThreadGroup group) {
        this.namePrefix = namePrefix;
        this.group = group;
        this.count = new AtomicLong();
    }

    DefaultThreadFactory(final String namePrefix) {
        this(namePrefix, null);
    }

    @Override
    public Thread newThread(final Runnable target) {
        return new Thread(this.group, target, this.namePrefix + "-" + this.count.incrementAndGet());
    }
}