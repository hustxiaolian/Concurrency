package com.xiaolianhust.concurrency.thinkingInjava;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadExecutor {
	public static void main(String[] args) {
		ExecutorService exec = Executors.newSingleThreadExecutor();
		for(int i = 0;i < 5;++i) {
			exec.execute(new LiftOff());
		}
		System.out.println("shutdown.");//单线程执行器，它会排队一次执行提交的线程任务
		exec.shutdown();
	}
}
