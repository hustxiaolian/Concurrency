package com.xiaolianhust.concurrency.thinkingInjava;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class E8_SimpleThread {
	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newCachedThreadPool(new DaemonThreadFactory());
		for(int i = 0;i < 5;++i) {
			exec.execute(new LiftOff());
		}
		exec.shutdown();
		TimeUnit.MILLISECONDS.sleep(1);
	}
}
