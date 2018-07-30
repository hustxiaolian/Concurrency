package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DaemonFromFactory implements Runnable {

	@Override
	public void run() {
		try {
			TimeUnit.MILLISECONDS.sleep(100);
			System.out.println(Thread.currentThread() + " " + this);
		} catch (Exception e) {
			System.out.println("Interrupt!");
		}
	}
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool(new DaemonThreadFactory());
		//这里传递过去的ThreadFactory方法，这里应该是用了某种设计模式。由于传递了这个工厂对象，因此所有的传递的task都会被设置成后台线程得到执行
		for(int i = 0;i < 10;++i) {
			exec.execute(new DaemonFromFactory());
		}
		
		System.out.println("all start!");
		try {
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
