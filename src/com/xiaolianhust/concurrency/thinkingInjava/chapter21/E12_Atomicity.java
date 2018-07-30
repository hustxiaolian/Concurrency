package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class E12_Atomicity implements Runnable{
	private int i = 0;
	public synchronized int getValue() {return i;}
	
	public synchronized void add() {i++;i++;}

	@Override
	public void run() {
		while(true)
			add();
	}
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		E12_Atomicity test = new E12_Atomicity();
		exec.execute(test);
		
		while(true) {
			int val = test.getValue();
			if(val % 2 != 0) {
				System.out.println(val);
				System.exit(1);
			}
		}
		
	}
}
