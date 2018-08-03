package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class E21_Task1 implements Runnable{

	@Override
	public synchronized void run() {
		try {
			wait();
			System.out.println(getClass().getSimpleName() + ": hello,xiaolian" );
		} catch (InterruptedException e) {
			System.out.println(getClass().getSimpleName() + " interrupted from wait()");
		}
	}
	
}


class E21_Task2 implements Runnable{
	private Runnable r1;
	
	public E21_Task2(Runnable r1) {
		super();
		this.r1 = r1;
	}
	@Override
	public void run() {
		synchronized (r1) {
			try {
				TimeUnit.SECONDS.sleep(5);
				r1.notifyAll();
			} catch (InterruptedException e) {
				System.out.println(getClass().getSimpleName() + " interrupted from sleep()");
			}
		}
	}
	
}

public class E21_WaitNotifyTest {

	public static void main(String[] args) {
		ExecutorService  exec = Executors.newCachedThreadPool();
		Runnable r1 = new E21_Task1();
		exec.execute(r1);
		exec.execute(new E21_Task2(r1));
		
		
	}

}
