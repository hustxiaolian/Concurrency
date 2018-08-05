package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class Toast{
	enum Status {DRY, BUTTERED, JAMMED}
	private Status status;
	private final int id;
	
	public Toast(int id) {
		super();
		this.id = id;
		status = Status.DRY;
	}

	public Status getStatus() {
		return status;
	}

	public int getId() {
		return id;
	}
	
	public void buttered() {
		status = Status.BUTTERED;
	}
	
	public void jammed() {
		status = Status.JAMMED;
	}

	@Override
	public String toString() {
		return "Toast " + id + ": " + status;
	}
}

@SuppressWarnings("serial")
class ToastQueue extends LinkedBlockingQueue<Toast> {}

/**
 * ������
 * @author 25040
 *
 */
class Toaster implements Runnable{
	private ToastQueue toastQueue;
	private int count = 0;
	private Random rand = new Random();
	
	public Toaster(ToastQueue toastQueue) {
		super();
		this.toastQueue = toastQueue;
	}

	@Override
	public void run() {
		try {
			while(! Thread.interrupted()) {
				TimeUnit.MILLISECONDS.sleep(500 + rand.nextInt(1000));//ģ����������Ĺ���
				Toast t = new Toast(count++);
				System.out.println(this + "�����һ��" + t);
				toastQueue.put(t);
			}
		} catch (InterruptedException e) {
			System.out.println(this + " interrupted...");
		}
	}
	
	public String toString() {
		return "��˾������: ";
	}
}

class Butterer implements Runnable{
	private ToastQueue dryToastQueue;
	private ToastQueue butteredToastQueue;
	private Random rand = new Random();
	
	public Butterer(ToastQueue dryToastQueue, ToastQueue butteredToastQueue) {
		super();
		this.dryToastQueue = dryToastQueue;
		this.butteredToastQueue = butteredToastQueue;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				Toast t = dryToastQueue.take();
				TimeUnit.MILLISECONDS.sleep(500 + rand.nextInt(1000));//ģ����˾�ϻ��͵Ĺ���
				t.buttered();
				System.out.println(this  + "" + t + "Ϳ���˻���");
				butteredToastQueue.put(t);
			}
		} catch (InterruptedException e) {
			System.out.println(this + " interrupted...");
		}
	}
	
	public String toString() {
		return "\t��˾���ͻ�: ";
	}
}

class Jammer implements Runnable{
	private ToastQueue butteredQueue;
	private ToastQueue finishQueue;
	private Random rand = new Random();
	
	public Jammer(ToastQueue butteredQueue, ToastQueue finishQueue) {
		super();
		this.butteredQueue = butteredQueue;
		this.finishQueue = finishQueue;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				Toast t = butteredQueue.take();
				TimeUnit.MILLISECONDS.sleep(500 + rand.nextInt(1000));//ģ��Ϳ�����Ĺ���
				t.jammed();
				System.out.println(this + "��" + t + "Ϳ�˹���");
				finishQueue.put(t);
			}
		} catch (InterruptedException e) {
			System.out.println(this + " interrupted...");
		}
	}
	
	public String toString() {
		return "\t\t��˾������: ";
	}
}

class Customer implements Runnable{
	private ToastQueue finishQueue;
	private int count = 0;
	
	public Customer(ToastQueue finishQueue) {
		super();
		this.finishQueue = finishQueue;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				Toast t = finishQueue.take();
				if(t.getStatus() != Toast.Status.JAMMED || t.getId() != count++) {
					System.out.println("Error,exit");
					System.exit(1);
				}
				System.out.println(this + "���ٳ���һ����˾...");
			}
		} catch (InterruptedException e) {
			System.out.println(this + " interrupted...");
		}
	}
	
	@Override
	public String toString() {
		return "\t\t\t�Ի�: ";
	}
	
}

public class ToastOMatic {
	private ToastQueue dryQueue = new ToastQueue();
	private ToastQueue butteredQueue = new ToastQueue();
	private ToastQueue finishQueue = new ToastQueue();
	private Toaster toaster = new Toaster(dryQueue);
	private Butterer butterer = new Butterer(dryQueue, butteredQueue);
	private Jammer jammer = new Jammer(butteredQueue, finishQueue);
	private Customer cus = new Customer(finishQueue);
	
	public void test() throws InterruptedException {
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(toaster);
		exec.execute(butterer);
		exec.execute(jammer);
		exec.execute(cus);
		
		TimeUnit.SECONDS.sleep(50);
		
		exec.shutdownNow();
	}
	
	public static void main(String[] args) throws InterruptedException {
		new ToastOMatic().test();
	}

}
