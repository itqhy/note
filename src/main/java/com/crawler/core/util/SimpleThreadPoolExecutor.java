package com.crawler.core.util;

import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/8/24.
 */
public class SimpleThreadPoolExecutor extends ThreadPoolExecutor{
    private String threadPoolName;

    public SimpleThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, String threadPoolName) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.threadPoolName = threadPoolName;
    }

    public SimpleThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, String threadPoolName) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        this.threadPoolName = threadPoolName;
    }

    public SimpleThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler, String threadPoolName) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        this.threadPoolName = threadPoolName;
    }

    public SimpleThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler, String threadPoolName) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        this.threadPoolName = threadPoolName;
    }

    /**
     * 修改thread name
     * @param t
     * @param r
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        if (t.getName().startsWith("pool-")){
            t.setName(t.getName().replaceAll("pool-\\d", this.threadPoolName));
        }
    }
}
