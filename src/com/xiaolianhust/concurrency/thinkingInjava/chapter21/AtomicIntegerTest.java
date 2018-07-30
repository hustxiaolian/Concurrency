package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTest implements Runnable {
	private AtomicInteger a = new AtomicInteger(0);
	
	public void next() {a.addAndGet(2);}

	public int getA() {
		return a.get();
	}
	
	@Override
	public void run() {
		while(true)
			next();
	}

	public static void main(String[] args) {
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				System.out.println("Aborting...");
				System.exit(1);
			}
		}, 3000);
		
		ExecutorService exec = Executors.newCachedThreadPool();
		AtomicIntegerTest test = new AtomicIntegerTest();
		exec.execute(test);
		
		while(true) {
			int temp = test.getA();
			if(temp % 2 != 0) {
				System.out.println("´íÎó");
				System.exit(1);
			}
		}
	}

}
