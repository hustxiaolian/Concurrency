package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class TaskPortion implements Runnable{
	private static int idCounter = 0;
	private final int id = idCounter++;
	private final CountDownLatch latch;
	private static Random rand = new Random();
	
	public TaskPortion(CountDownLatch latch) {
		super();
		this.latch = latch;
	}
	
	public void doWork() throws InterruptedException {
		TimeUnit.MILLISECONDS.sleep(rand.nextInt(2000));
		System.out.println(this + "work done!");
	}
	
	@Override
	public void run() {
		try {
			doWork();
			latch.countDown();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public String toString() {
		return String.format("Task Portion %1$-3d", id);
	}
}

class WaitingTask implements Runnable{
	private static int idCounter = 0;
	private final int id = idCounter++;
	private final CountDownLatch latch;
	
	public WaitingTask(CountDownLatch latch) {
		super();
		this.latch = latch;
	}
	
	@Override
	public void run() {
		try {
			latch.await();
			System.out.println(this + "start work...");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public String toString() {
		return String.format("WaitingTask %1$-3d", id);
	}
	
}

public class CountDownLatchDemo {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newCachedThreadPool();
		CountDownLatch latch = new CountDownLatch(100);
		for(int i = 0;i < 10;++i) {
			exec.execute(new WaitingTask(latch));
		}
		for(int i = 0;i < 100;++i)
			exec.execute(new TaskPortion(latch));
		
		TimeUnit.SECONDS.sleep(5);
		
		exec.shutdownNow();
	}

}
