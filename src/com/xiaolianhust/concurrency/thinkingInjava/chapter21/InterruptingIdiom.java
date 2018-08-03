package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.concurrent.TimeUnit;

class NeedsCleanup{
	private int id;

	public NeedsCleanup(int id) {
		super();
		this.id = id;
		System.out.println("NeedsCleanup " + id);
	}
	
	public void cleanup() {
		System.out.println("Cleanup " + id);
	}
}

class Block3 implements Runnable{
	private volatile double d = 1.0;
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				NeedsCleanup n1 = new NeedsCleanup(1);
				try {
					System.out.println("Sleeping...");
					TimeUnit.SECONDS.sleep(5);
					System.out.println("Sleeping done!");
					NeedsCleanup n2 = new NeedsCleanup(2);
					try {
						for(int i = 0;i < 250000;++i)
							d = d + (Math.PI + Math.E) / d;
						System.out.println("Finish caculation...");
					} finally {
						n2.cleanup();
					}
				} finally {
					n1.cleanup();
				}
			}
			System.out.println("exit via while() test!...");
		} catch (InterruptedException e) {
			System.out.println("exit via interruptException...");
		}
	}
	
}

public class InterruptingIdiom {

	public static void main(String[] args) throws InterruptedException {
		Thread t = new Thread(new Block3());
		t.start();
		
		TimeUnit.MILLISECONDS.sleep(5001);
		t.interrupt();
		
		
	}

}
