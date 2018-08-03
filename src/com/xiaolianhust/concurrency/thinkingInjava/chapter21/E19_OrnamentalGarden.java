package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class E19_Count{
	private int count = 0;
	private Random rand = new Random();
	
	public int increment() {
		int temp = count;
		if(rand.nextBoolean())
			Thread.yield();
		return (count = ++temp);
	}
}


class E19_Entrance implements Runnable{
	private static E19_Count count = new E19_Count();
	private int currNumber = 0;
	private static int entranCounter = 1;
	private final int id = entranCounter++;

	/**
	 * 以当前对象为锁，反正多任务同时访问currNumber,其实我感觉，不会出错，但是稳重点更好
	 * @return
	 */
	public synchronized int getCurrEntranceNumber() {return currNumber;}
	
	@Override
	public void run() {
		try {
			while(true) {
				synchronized (count) {
					++currNumber;
					System.out.println(this + " Total:" + count.increment());
				}
				TimeUnit.SECONDS.sleep(2);
			}
		} catch (InterruptedException e) {
			System.out.println("Entrance#" + id + " stopped by interrupt...");
		}finally {
			System.out.println("Entrance#" + id + " cleaning...");
		}
	}

	@Override
	public String toString() {
		return "E19_Entrance " + id + ":" + currNumber;
	}
	
	
}

public class E19_OrnamentalGarden{
	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i = 0;i < 5;++i) {
			exec.execute(new E19_Entrance());
		}
		TimeUnit.SECONDS.sleep(11);
		exec.shutdownNow();
		TimeUnit.SECONDS.sleep(5);
		System.out.println("system.exit...");
		System.exit(0);
	}
}