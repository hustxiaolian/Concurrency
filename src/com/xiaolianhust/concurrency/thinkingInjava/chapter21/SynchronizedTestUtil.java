package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SynchronizedTestUtil {
	public static void test(Runnable task, int testThreadNum) {
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i = 0;i < testThreadNum;++i)
			exec.execute(task);
		exec.shutdown();
	}
}
