package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class E26_WaitPerson implements Runnable{
	E26_Restaurant restaurant;
	
	public E26_WaitPerson(E26_Restaurant restaurant) {
		super();
		this.restaurant = restaurant;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				System.out.println(this + "等待厨师做菜...");
				synchronized (restaurant.w) {
					while(restaurant.meal == null)
						restaurant.w.wait();
				}
				System.out.println(this + "拿到菜,送给客人");
				synchronized (restaurant.c) {
					System.out.println(this + "通知厨师可以上下一道菜了");
					restaurant.meal = null;
					restaurant.c.notifyAll();
				}
				System.out.println(this + "客人正在用餐...");
				TimeUnit.SECONDS.sleep(2);
				System.out.println(this + "客人用餐完毕");
				
				System.out.println(this + "通知清洁员清理餐桌");
				synchronized (restaurant.b) {
					restaurant.needClean = true;
					restaurant.b.notifyAll();
				}
				System.out.println(this + "等到清洁员清理餐桌");
				synchronized (restaurant.w) {
					while(restaurant.needClean)
						restaurant.w.wait();
				}
				System.out.println(this + "确认餐桌干净，去拿下一道菜");
				
			}
		} catch (InterruptedException e) {
			System.out.println("WaitPersion interrupted...");
		}
	}
	
	@Override
	public String toString() {
		return "服务员：";
	}
}

class E26_Chef implements Runnable{
	E26_Restaurant restaurant;
	
	public E26_Chef(E26_Restaurant restaurant) {
		super();
		this.restaurant = restaurant;
	}

	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				System.out.println(this + "正在做菜...");
				TimeUnit.SECONDS.sleep(2);//模拟厨师做菜的时间
				System.out.println(this + "菜做好了，通知服务员");
				synchronized (restaurant.w) {
					restaurant.meal = new Meal(1);
					restaurant.w.notifyAll();
				}
				System.out.println(this + "等着做下一道菜");
				synchronized (restaurant.c) {
					while(restaurant.meal != null)
						restaurant.c.wait();
				}
			}
		} catch (InterruptedException e) {
			System.out.println("Chef interrupted...");
		}
	}
	
	@Override
	public String toString() {
		return "\t厨师：";
	}
	
}

class E26_BusBoy implements Runnable{
	E26_Restaurant restaurant;
	
	public E26_BusBoy(E26_Restaurant restaurant) {
		super();
		this.restaurant = restaurant;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				System.out.println(this + "检查餐桌是否干净");
				synchronized (restaurant.b) {
					while(!restaurant.needClean)
						restaurant.b.wait();
				}
				System.out.println(this + "正在清理餐桌");
				TimeUnit.SECONDS.sleep(1);
				System.out.println(this + "餐桌清理完毕，通知服务员");
				synchronized (restaurant.w) {
					restaurant.needClean = false;
					restaurant.w.notifyAll();
				}
				
			}
		} catch (InterruptedException e) {
			
		}
	}
	
	@Override
	public String toString() {
		return "\t\t清洁员：";
	}
}

public class E26_Restaurant {
	Meal meal = new Meal(1);
	boolean needClean = false;
	E26_WaitPerson w = new E26_WaitPerson(this);
	E26_Chef c = new E26_Chef(this);
	E26_BusBoy b = new E26_BusBoy(this);
	public static void main(String[] args) throws InterruptedException {
		E26_Restaurant r = new E26_Restaurant();
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new E26_WaitPerson(r));
		exec.execute(new E26_Chef(r));
		exec.execute(new E26_BusBoy(r));
		
		TimeUnit.SECONDS.sleep(50);
		
		exec.shutdownNow();
		
		
	}

}
