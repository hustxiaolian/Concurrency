package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class E22_Flag{
	private boolean flag;

	public synchronized boolean isFlag() {
		return flag;
	}

	public synchronized void setFlag(boolean flag) {
		this.flag = flag;
	}
}

class E22_Task1 implements Runnable{
	private E22_Flag flag;
	
	public E22_Task1(E22_Flag flag) {
		super();
		this.flag = flag;
	}

	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				TimeUnit.SECONDS.sleep(2);
				flag.setFlag(true);
			}
		}catch (InterruptedException e) {
			// TODO: handle exception
		}
	}
	
}

class E22_Task2 implements Runnable{
	private E22_Flag flag;
	
	public E22_Task2(E22_Flag flag) {
		super();
		this.flag = flag;
	}
	@Override
	public void run() {
		while(!flag.isFlag()) {
			System.out.println("checking flag...");
		}
		System.out.println("Ok");
		flag.setFlag(true);
	}
	
}

class E22_Task3 implements Runnable{
	private E22_Flag flag;
	
	public E22_Task3(E22_Flag flag) {
		super();
		this.flag = flag;
	}
	@Override
	public void run() {
		try {
			System.out.println("Sleeping");
			TimeUnit.SECONDS.sleep(5);
			synchronized (flag) {
				flag.setFlag(true);
				flag.notifyAll();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
}

class E22_Task4 implements Runnable{
	private E22_Flag flag;
	
	public E22_Task4(E22_Flag flag) {
		super();
		this.flag = flag;
	}
	@Override
	public void run() {
		try {
			System.out.println("wait for flag = true");
			synchronized (flag) {
				while(!flag.isFlag())
					flag.wait();
			}
			System.out.println("Ok");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

public class E22_BusyWaitTest {
	private static E22_Flag flag = new E22_Flag();
	public static void main(String[] args) {
		ExecutorService exec = Executors.newFixedThreadPool(2);
		exec.execute(new E22_Task3(flag));
		exec.execute(new E22_Task4(flag));
		
		
	}
}
