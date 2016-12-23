package com.sfg.administrator.searchfromgithub.global;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThreadPoolManager {

	private MyThreadPoolManager() {
	}

	private static MyThreadPoolManager instance;

	public synchronized static MyThreadPoolManager getInstance() {
		if (instance == null) {
			instance = new MyThreadPoolManager();
		}
		return instance;
	}

	private ThreadPoolExecutor mExecutor;

	public void execute(Runnable r) {
		if (mExecutor == null) {
			// 创建线程池
			/**
			 * corePoolSize：这个池子里面的核心线程数量。正常情况下，池子里面同时运行的线程数量
			 * maximumPoolSize：workqueue满的情况下，池子里面同时运行的线程数量
			 * keepAliveTime：空闲时间，线程执行完当前任务，到执行下一个任务的休息时间 unit：空闲时间单位
			 * workQueue：队列，存的是，等待中的任务 threadFactory：创建线程的工厂类 handler：异常处理机制
			 * 
			 */
			mExecutor = new ThreadPoolExecutor(5, 10, 0, TimeUnit.SECONDS,
					new ArrayBlockingQueue<Runnable>(20),
					Executors.defaultThreadFactory(),
					new ThreadPoolExecutor.AbortPolicy());
		}

		mExecutor.execute(r);// 将runnable对象放到池子里面去
	}
	
	
	public void cancel(Runnable r) {
		if(r != null) {
			//得到workqueue
			mExecutor.getQueue().remove(r);
		}
	}

}