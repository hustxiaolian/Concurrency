package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

public class E1_ThreatTest implements Runnable{
	public static int taskCount;
	public final int id = taskCount++;
	
	public E1_ThreatTest() {
		System.out.println("E1_ThreatTest "+ id + " start...");
	}
	
	@Override
	public void run() {
		System.out.println(id + ": info 1");
		Thread.yield();
		System.out.println(id + ": info 2");
		Thread.yield();
		System.out.println(id + ": info 3");
		Thread.yield();
		
		System.out.println("Done!");
	}
	
	public static void main(String[] args) {
		for(int i = 0;i < 10;++i) {
			new Thread(new E1_ThreatTest()).start();
		}
	}
	
}	
