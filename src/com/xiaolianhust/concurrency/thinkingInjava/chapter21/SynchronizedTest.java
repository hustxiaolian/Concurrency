package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class TaskTest implements Runnable{
	private int i = 1;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			read();
		}
	}
	
	private synchronized void read() {
		System.out.println(Thread.currentThread() + "reading...");
		try {
			TimeUnit.MILLISECONDS.sleep(500);
			write();
		} catch (InterruptedException e) {
			System.out.println("reading interrupted");
			return;
		}
		System.out.println(i++);
	}
	
	
	private synchronized void write() {
		System.out.println(Thread.currentThread() + "writing...");
	}
}

public class SynchronizedTest {

	public static void main(String[] args) {
		Runnable task = new TaskTest();
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i = 0;i < 5;++i) {
			exec.execute(task);
		}
		exec.shutdown();
	}

}
