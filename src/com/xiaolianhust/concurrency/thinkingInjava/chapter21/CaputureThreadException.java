package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

class ExceptionThread implements Runnable{
	@Override
	public void run() {
		Thread t = Thread.currentThread();
		System.out.println("run by" + t);
		System.out.println("eh = " + t.getUncaughtExceptionHandler());
		throw new RuntimeException("hi");
	}
}

class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{
	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
		System.out.println("caught " + arg1);
	}
}

class ExecptionThreadFactory implements ThreadFactory{
	@Override
	public Thread newThread(Runnable arg0) {
		Thread t = new Thread(arg0);
		t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
		return t;
	}
}

public class CaputureThreadException {
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool(new ExecptionThreadFactory());
		exec.execute(new ExceptionThread());
	}
}
