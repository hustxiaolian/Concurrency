package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

public class LiftOff implements Runnable {
	public int countDown = 10;
	public static int taskCount = 0;
	public final int taskId = taskCount++;
	
	public LiftOff(int countDown) {
		super();
		this.countDown = countDown;
	}

	public LiftOff() {}

	@Override
	public void run() {
		while(countDown-- > 0) {
			System.out.println(status());
			Thread.yield();
		}
	}

	private String status() {
		return "#" + taskId + "(" + (countDown > 0 ? countDown:"LiftOff") + ").";
	}
	
	public static void main(String[] args) {
		//Thread t = new Thread(new LiftOff());
		//t.start();
		
		for(int i = 0;i < 5;++i) {
			new Thread(new LiftOff()).start();
		}
	}

}
