package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Meal{
	private final int orderNum;
	public Meal(int orderNum) {
		super();
		this.orderNum = orderNum;
	}
	@Override
	public String toString() {
		return "Meal [" + orderNum + "]";
	}
}

class WaitPerson implements Runnable{
	Restaurant restaurant;
	
	public WaitPerson(Restaurant restaurant) {
		super();
		this.restaurant = restaurant;
	}

	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				synchronized (restaurant.w) {
					while(restaurant.meal == null)
						wait();
				}
				System.out.println("WaitPerson got" + restaurant.meal);
				synchronized (restaurant.c) {
					restaurant.meal = null;
					restaurant.c.notifyAll();
				}
			}
			System.out.println("exit via while() test");
		} catch (InterruptedException e) {
			System.out.println("WaitPersion interrupted...");
		}
	}
}

class Chef implements Runnable{
	Restaurant restaurant;
	
	public Chef(Restaurant restaurant) {
		super();
		this.restaurant = restaurant;
	}
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				TimeUnit.SECONDS.sleep(2);//模拟厨师做菜的时间
				synchronized (restaurant.w) {
					restaurant.meal = new Meal(1);
					restaurant.w.notifyAll();
				}
				System.out.println("Chef product a meal");
				synchronized (restaurant.c) {
					while(restaurant.meal != null)
						wait();
				}
			}
		} catch (InterruptedException e) {
			System.out.println("Chef interrupted...");
		}
	}
	
}

public class Restaurant {
	Meal meal;
	WaitPerson w = new WaitPerson(this);
	Chef c = new Chef(this);
	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newFixedThreadPool(2);
		Restaurant r = new Restaurant();
		exec.execute(r.w);
		exec.execute(r.c);
		
		TimeUnit.SECONDS.sleep(20);
		
		exec.shutdownNow();
	}

}
