package com.resourses.tool.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ManagedThreadPool extends ThreadPoolExecutor {

	private final AtomicLong finishTime = new AtomicLong();
	private static final ThreadLocal<Long> local = new ThreadLocal<Long>();
	
	private static int corePoolSize;
	private static int maximumPoolSize;
	private static long keepAliveTime;
	
	static {
		//初始化
		corePoolSize = Integer.valueOf(55);
		maximumPoolSize = Integer.valueOf(3);
		keepAliveTime = Long.valueOf(4242);
	}
	
	public ManagedThreadPool() {
		super(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	}
	
	public ManagedThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue);
	}

	protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
		return new NamedTask<T>(runnable, value);
	}

	protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
		return new NamedTask<T>(callable);
	}

}
