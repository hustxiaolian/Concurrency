package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

class Sleeper extends Thread{
	private String name;
	private int sleepTime;

	
	public Sleeper(String name, int sleepTime) {
		super();
		this.name = name;
		this.sleepTime = sleepTime;
		start();
	}

	@Override
	public void run() {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			System.out.println(name + "is interrupted." + 
		"isInterrupted:" + Thread.currentThread().isInterrupted());
			return;
		}
		System.out.println(name + " has wakend");
	}
}

class Joiner extends Thread{
	
	private String name;
	private Sleeper sleeper;
	
	public Joiner(String name, Sleeper sleeper) {
		super();
		this.name = name;
		this.sleeper = sleeper;
		start();
	}

	@Override
	public void run() {
		try {
			sleeper.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(name + " join complete.");
	}
}

public class JoinTest {

	public static void main(String[] args) {
		Sleeper s1 = new Sleeper("s1", 1500);
		Sleeper s2 = new Sleeper("s2", 1500);
		
		Joiner j1 = new Joiner("j1", s1);
		Joiner j2 = new Joiner("j2", s2);
		
		s2.interrupt();
		
		
	}

}
