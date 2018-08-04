package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class LiftOffRunner implements Runnable{
	private BlockingQueue<LiftOff> rockets;
	
	public LiftOffRunner(BlockingQueue<LiftOff> rockets) {
		super();
		this.rockets = rockets;
	}
	
	public void add(LiftOff lo) {
		try {
			rockets.put(lo);
		} catch (InterruptedException e) {
			System.out.println("Interrupted during put()");
		}
	}
	
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				LiftOff rocket = rockets.take();
				rocket.run();
			}
		} catch (InterruptedException e) {
			System.out.println("Waking from take()");
		}
		System.out.println("exitint LiftOffRunner");
	}
	
}

class E28_LiftOffProducter implements Runnable{
	private BlockingQueue<LiftOff> rockets;
	
	public E28_LiftOffProducter(BlockingQueue<LiftOff> rockets) {
		super();
		this.rockets = rockets;
	}
	
	public void add(LiftOff lo) {
		try {
			rockets.put(lo);
		} catch (InterruptedException e) {
			System.out.println("Interrupted during put()");
		}
	}
	@Override
	public void run() {
		int counter = 5;
		try {
			while(!Thread.interrupted()) {
				add(new LiftOff(++counter));
				TimeUnit.SECONDS.sleep(5);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
} 

public class TestBlockingQueues {
	static void test(BlockingQueue<LiftOff> queue) throws InterruptedException {
		LiftOffRunner runner = new LiftOffRunner(queue);
		Thread t = new Thread(runner);
		t.start();
		for(int i = 0;i < 5;++i)
			runner.add(new LiftOff(5));
		
		TimeUnit.SECONDS.sleep(20);
		
		t.interrupt();
		
	}
	
	static void test2(BlockingQueue<LiftOff> queue) throws InterruptedException {
		LiftOffRunner runner = new LiftOffRunner(queue);
		E28_LiftOffProducter producter = new E28_LiftOffProducter(queue);
		ExecutorService exec = Executors.newFixedThreadPool(2);
		exec.execute(runner);
		exec.execute(producter);
		
		TimeUnit.SECONDS.sleep(100);
		
		exec.shutdownNow();
	}
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		test2(new LinkedBlockingQueue<>());
	}

}
