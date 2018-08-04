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
				System.out.println(this + "�ȴ���ʦ����...");
				synchronized (restaurant.w) {
					while(restaurant.meal == null)
						restaurant.w.wait();
				}
				System.out.println(this + "�õ���,�͸�����");
				synchronized (restaurant.c) {
					System.out.println(this + "֪ͨ��ʦ��������һ������");
					restaurant.meal = null;
					restaurant.c.notifyAll();
				}
				System.out.println(this + "���������ò�...");
				TimeUnit.SECONDS.sleep(2);
				System.out.println(this + "�����ò����");
				
				System.out.println(this + "֪ͨ���Ա�������");
				synchronized (restaurant.b) {
					restaurant.needClean = true;
					restaurant.b.notifyAll();
				}
				System.out.println(this + "�ȵ����Ա�������");
				synchronized (restaurant.w) {
					while(restaurant.needClean)
						restaurant.w.wait();
				}
				System.out.println(this + "ȷ�ϲ����ɾ���ȥ����һ����");
				
			}
		} catch (InterruptedException e) {
			System.out.println("WaitPersion interrupted...");
		}
	}
	
	@Override
	public String toString() {
		return "����Ա��";
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
				System.out.println(this + "��������...");
				TimeUnit.SECONDS.sleep(2);//ģ���ʦ���˵�ʱ��
				System.out.println(this + "�������ˣ�֪ͨ����Ա");
				synchronized (restaurant.w) {
					restaurant.meal = new Meal(1);
					restaurant.w.notifyAll();
				}
				System.out.println(this + "��������һ����");
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
		return "\t��ʦ��";
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
				System.out.println(this + "�������Ƿ�ɾ�");
				synchronized (restaurant.b) {
					while(!restaurant.needClean)
						restaurant.b.wait();
				}
				System.out.println(this + "�����������");
				TimeUnit.SECONDS.sleep(1);
				System.out.println(this + "����������ϣ�֪ͨ����Ա");
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
		return "\t\t���Ա��";
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
