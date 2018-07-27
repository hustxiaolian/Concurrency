package com.xiaolianhust.concurrency.thinkingInjava;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedThreadPool {
	public static void main(String[] args) {
		ExecutorService exec = Executors.newFixedThreadPool(5);
		for(int i = 0;i < 5;++i) {
			exec.execute(new LiftOff());
		}
		System.out.println("shutdown.");//也就是说它一次性先把各线程创建好，然后shutdown后一起执行
		exec.shutdown();
	}
}
