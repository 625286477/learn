### ThreadPoolExecutor 学习笔记

#### 继承关系
<pre>
Executor  execute(Runnable command);
    |  
    |继承
    V  
ExecutorService  shutdown();shutdownNow();isShutdown();isTerminated(); submit(Runnable task); 
    |            submit(Callable<T> task);submit(Runnable task, T result);等等
    |实现        
    V 
AbstractExecutorService  
    |  
    |继承
    V 
ThreadPoolExecutor ---封装成---> Executors
    |  
    |继承
    V 
ScheduledThreadPoolExecutor <--实现--- ScheduledExecutorService        
</pre>
### 构造函数
一共有四个构造函数，最终都指向其中的一个。源码如下
```
    /**
     * Creates a new {@code ThreadPoolExecutor} with the given initial
     * parameters.
     *
     * @param corePoolSize the number of threads to keep in the pool, even
     *        if they are idle, unless {@code allowCoreThreadTimeOut} is set
     *        核心线程数
     * @param maximumPoolSize the maximum number of threads to allow in the
     *        pool
     *        最大线程数
     * @param keepAliveTime when the number of threads is greater than
     *        the core, this is the maximum time that excess idle threads
     *        will wait for new tasks before terminating.
     * @param unit the time unit for the {@code keepAliveTime} argument
     * @param workQueue the queue to use for holding tasks before they are
     *        executed.  This queue will hold only the {@code Runnable}
     *        tasks submitted by the {@code execute} method.
     *        阻塞队列
     * @param threadFactory the factory to use when the executor
     *        creates a new thread
     *        线程工厂
     * @param handler the handler to use when execution is blocked
     *        because the thread bounds and queue capacities are reached
     *        拒绝策略
     * @throws IllegalArgumentException if one of the following holds:<br>
     *         {@code corePoolSize < 0}<br>
     *         {@code keepAliveTime < 0}<br>
     *         {@code maximumPoolSize <= 0}<br>
     *         {@code maximumPoolSize < corePoolSize}
     * @throws NullPointerException if {@code workQueue}
     *         or {@code threadFactory} or {@code handler} is null
     */
    public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                              BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) {
        if (corePoolSize < 0 || maximumPoolSize <= 0 || maximumPoolSize < corePoolSize || keepAliveTime < 0)
            throw new IllegalArgumentException();
        if (workQueue == null || threadFactory == null || handler == null)
            throw new NullPointerException();
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
        this.keepAliveTime = unit.toNanos(keepAliveTime);
        this.threadFactory = threadFactory;
        this.handler = handler;
    }   
 ```
+ corePoolSize  
核心线程数，默认情况下核心线程会一直存活，即使处于闲置状态也不会受存 keepAliveTime 限制。除非将 allowCoreThreadTimeOut 设置为 true。
+ maximumPoolSize  
线程池最大线程数，这个参数也是一个非常重要的参数，它表示在线程池中最多能创建多少个线程；
   + 如果运行的线程少于 corePoolSize，则创建新线程来处理请求，即使其他辅助线程是空闲的；
   + 如果设置的 corePoolSize 和 maximumPoolSize 相同，则创建的线程池是大小固定的，如果运行的线程与 corePoolSize 相同，
   当有新请求过来时，若 workQueue 任务阻塞队列未满，则将请求放入 workQueue 中，等待有空闲的线程从 workQueue 中取出任务并处理。
   + 如果运行的线程多于 corePoolSize 而少于 maximumPoolSize，则仅当 workQueue 任务阻塞队列满时才创建新线程去处理请求；
   + 如果运行的线程多于 corePoolSize 并且等于 maximumPoolSize，若 workQueue 任务阻塞队列已满，则通过 handler 所指定的策略来处理新请求；
   + 如果将 maximumPoolSize 设置为基本的无界值（如 Integer.MAX_VALUE），则允许池适应任意数量的并发任务。  
   + 核心线程 corePoolSize > 阻塞队列 workQueue > 最大线程 maximumPoolSize，如果三者都满了，使用 handler 处理被拒绝的任务。
   + 当池中的线程数大于 corePoolSize 的时候，多余的线程会等待 keepAliveTime 长的时间，如果无请求处理，就自行销毁。
+ keepAliveTime  
非核心线程的闲置超时时间，超过这个时间就会被回收。
+ unit  
指定 keepAliveTime 的单位，如 TimeUnit.SECONDS。当将 allowCoreThreadTimeOut 设置为true时对 corePoolSize 生效。
+ workQueue  
线程池中的任务队列。常用的有三种队列，SynchronousQueue, LinkedBlockingDeque, ArrayBlockingQueue。
+ threadFactory  
 线程工厂，提供创建新线程的功能。ThreadPoolExecutor 类中默认使用 Executors.defaultThreadFactory() 的默认线程工厂
```
public interface ThreadFactory {
    Thread newThread(Runnable r);
}
```
  
+ RejectedExecutionHandler  
在ThreadPoolExecutor类中实现了4种拒绝策略  
   + AbortPolicy 直接抛出异常，该策略是 ThreadPoolExecutor 的默认策略
   ```
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        throw new RejectedExecutionException("Task " + r.toString() + " rejected from " + e.toString());
    }   
   ```
   + CallerRunsPolicy 只要线程池未关闭，则直接运行当前被丢弃的任务。
   ```
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        if (!e.isShutdown()) {
            r.run();
        }
    }
   ```
   + DiscardOldestPolicy 抛弃队列中等待醉酒的一个线程，然后把任务加到队列，即淘汰最老的任务
   ```
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        if (!e.isShutdown()) {
            e.getQueue().poll();
            e.execute(r);
        }
    }
   ```
   + DiscardPolicy 直接丢弃，不处理
   ```
   public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
   }
   ```
### 子类 ScheduledThreadPoolExecutor
 ScheduledThreadPoolExecutor extends ThreadPoolExecutor implements ScheduledExecutorService
   
### Executors
Executors 其实就是对 ThreadPoolExecutor 做了一层封装，主要提供以下方法
+ newCachedThreadPool 
```
public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
    return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                  60L, TimeUnit.SECONDS,
                                  new SynchronousQueue<Runnable>(),
                                  threadFactory);
}
```
+ newFixedThreadPool
```
public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
    return new ThreadPoolExecutor(nThreads, nThreads,
                                  0L, TimeUnit.MILLISECONDS,
                                  new LinkedBlockingQueue<Runnable>(),
                                  threadFactory);
}
```
+ newScheduledThreadPool
```
public static ScheduledExecutorService newScheduledThreadPool(
        int corePoolSize, ThreadFactory threadFactory) {
    return new ScheduledThreadPoolExecutor(corePoolSize, threadFactory);
}
```
+ newSingleThreadExecutor
```
public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
    return new FinalizableDelegatedExecutorService
        (new ThreadPoolExecutor(1, 1,
                                0L, TimeUnit.MILLISECONDS,
                                new LinkedBlockingQueue<Runnable>(),
                                threadFactory));
}
```
+ newSingleThreadScheduledExecutor
```
public static ScheduledExecutorService newSingleThreadScheduledExecutor(ThreadFactory threadFactory) {
    return new DelegatedScheduledExecutorService
        (new ScheduledThreadPoolExecutor(1, threadFactory));
}
```
### 注意事项
+ 在实际使用的过程中不建议直接使用 Executors 来创建线程池，因为其都是执行的默认策略 AbortPolicy，使用的是 LinkedBlockingQueue 
阻塞队列，当任务比较耗时，短时间又一直添加大量的任务，容易造成资源的浪费。所以一般都是直接使用 ThreadPoolExecutor 来 new 出来，
重写拒绝策略。

