package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.sql.Time;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 表示一辆车。
 * @author 25040
 *
 */
class Car{
	private boolean waxOn = false;//表示打蜡是否完成
	
	/**
	 * 表示已经完成打蜡，通知车，
	 */
	public synchronized void waxed() {
		waxOn = true;
		this.notifyAll();
	}
	/**
	 * 表示已经完成抛光，通知车
	 */
	public synchronized void buffed() {
		waxOn = false;
		this.notifyAll();
	}
	/**
	 * 等待车完成抛光任务，即等待车此时，还未抛光，也就是true
	 * @throws InterruptedException 
	 */
	public synchronized void waitForBuffed() throws InterruptedException {
		while(waxOn == true) {
			wait();
		}
	}
	/**
	 * 等待车完成打蜡任务，因此，循环条件为waxOn为正在打蜡,还未完成打蜡，fasle
	 * @throws InterruptedException 
	 */
	public synchronized void waitForWaxed() throws InterruptedException {
		while(waxOn == false) {
			wait();
		}
	}
}

class WaxTask implements Runnable{
	private Car car;
	
	public WaxTask(Car car) {
		super();
		this.car = car;
	}

	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				System.out.println(this + "正在打蜡");
				TimeUnit.SECONDS.sleep(2);
				System.out.println(this + "打蜡完成，通知下车");
				car.waxed();
				System.out.println(this + "等待抛光任务完成");
				car.waitForBuffed();
			}
		} catch (InterruptedException e) {
			System.out.println("WaxTask exit via interruped...");
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": ";
	}
	
	
}

class BuffTask implements Runnable{
	private Car car;
	public BuffTask(Car car) {
		super();
		this.car = car;
	}
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				System.out.println(this + "等待车完成打蜡");
				car.waitForWaxed();
				System.out.println(this + "开始抛光");
				TimeUnit.SECONDS.sleep(2);
				System.out.println(this + "完成抛光，通知车");
				car.buffed();
			}
		} catch (InterruptedException e) {
			System.out.println("BuffTask exit via interruped...");
		}
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + ": ";
	}
	
}

public class WaxOMatic {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newCachedThreadPool();
		Car c = new Car();
		exec.execute(new WaxTask(c));
		exec.execute(new BuffTask(c));
		
		TimeUnit.SECONDS.sleep(50);
		exec.shutdownNow();
		TimeUnit.SECONDS.sleep(1);
		System.out.println("system done!exit!");
		System.exit(0);
	}

}
