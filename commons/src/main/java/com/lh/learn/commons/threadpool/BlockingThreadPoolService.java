package com.lh.learn.commons.threadpool;

import java.util.concurrent.*;

/**
 * 创建具有阻塞添加功能的线程池，即等待队列有界，防止系统资源被耗尽
 *
 * @author liuhai
 */
public class BlockingThreadPoolService {

    private BlockingThreadPoolService(){}

    /**
     * 创建只有一个工作线程的阻塞线程池
     *
     * @param namePrefix 线程名前缀
     * @param blockSize 阻塞队列大小
     */
    public static ThreadPoolExecutor newBlockingSingleThreadPool(String namePrefix, int blockSize) {
        return newBlockingFixedThreadPool(namePrefix, 1, blockSize);
    }


    public static ThreadPoolExecutor newBlockingFixedThreadPool(int nThreads, int blockSize) {
        return newBlockingFixedThreadPool(Executors.defaultThreadFactory(), nThreads, blockSize);
    }

    /**
     * 创建一个固定大小的线程的阻塞线程池
     *
     * @param namePrefix 线程名前缀
     * @param nThreads 核心线程数
     * @param blockSize 阻塞队列大小
     */
    public static ThreadPoolExecutor newBlockingFixedThreadPool(String namePrefix, int nThreads,
                                                                int blockSize) {
        return newBlockingFixedThreadPool(new DefaultThreadFactory(namePrefix), nThreads,
                blockSize);
    }


    /**
     * 创建一个固定大小的线程的阻塞线程池
     *
     * @param namePrefix 线程名前缀
     * @param threadGroup 线程组
     * @param nThreads 核心线程数
     * @param blockSize 阻塞队列大小
     */
    public static ThreadPoolExecutor newBlockingFixedThreadPool(String namePrefix,
                                                                ThreadGroup threadGroup, int nThreads,
                                                                int blockSize) {
        return newBlockingFixedThreadPool(new DefaultThreadFactory(namePrefix, threadGroup),
                nThreads, blockSize);
    }

    /**
     * 创建一个固定大小的线程的阻塞线程池
     *
     * @param threadFactory 线程工厂
     * @param nThreads 核心线程数
     * @param blockSize 阻塞队列大小
     */
    public static ThreadPoolExecutor newBlockingFixedThreadPool(ThreadFactory threadFactory,
                                                                int nThreads,
                                                                int blockSize) {
        return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(blockSize),
                threadFactory, (r, executor) -> {
            if (!executor.isShutdown()) {
                try {
                    executor.getQueue().put(r);
                } catch (InterruptedException e) {
                    //
                }
            }
        });
    }

}
