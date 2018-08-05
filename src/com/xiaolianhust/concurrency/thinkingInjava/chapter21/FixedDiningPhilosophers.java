package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Chopstick{
	private boolean taken = false;
	
	public synchronized void take() throws InterruptedException {
		while(taken)
			wait();
		taken = true;
	}
	
	public synchronized void drop() {
		taken = false;
		notifyAll();
	}
}

class Philosopher implements Runnable{
	private final int id;
	private Random rand = new Random();
	private final int ponderFactor;
	protected Chopstick left;
	protected Chopstick right;
	
	public Philosopher(int id, int ponderFactor, Chopstick left, Chopstick right) {
		super();
		this.id = id;
		this.ponderFactor = ponderFactor;
		this.left = left;
		this.right = right;
	}
	
	public void pause() throws InterruptedException {
		if(ponderFactor == 0) return;
		TimeUnit.MILLISECONDS.sleep(rand.nextInt(ponderFactor * 250));
	}
	
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				System.out.println(this + "����˼��...");
				pause();
				System.out.println(this + "���ˣ�׼���ÿ��ӳԷ�...");
				left.take();
				System.out.println(this + "��������ߵĿ���");
				right.take();
				System.out.println(this + "�������ұߵĿ���");
				System.out.println(this + "���ڳԷ�...");
				pause();
				right.drop();
				System.out.println(this + "������ߵĿ���");
				left.drop();
				System.out.println(this + "�������ұߵĿ���");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public String toString() {
		return "��ѧ��#" + id + "# :";
	}
}

class DiffPhilosopher extends Philosopher{

	public DiffPhilosopher(int id, int ponderFactor, Chopstick left, Chopstick right) {
		super(id, ponderFactor, left, right);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				System.out.println(this + "����˼��...");
				pause();
				System.out.println(this + "���ˣ�׼���ÿ��ӳԷ�...");
				right.take();
				System.out.println(this + "�������ұߵĿ���");
				left.take();
				System.out.println(this + "��������ߵĿ���");
				System.out.println(this + "���ڳԷ�...");
				pause();
				left.drop();
				System.out.println(this + "�������ұߵĿ���");
				right.drop();
				System.out.println(this + "������ߵĿ���");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}

public class FixedDiningPhilosophers {

	public static void main(String[] args) throws InterruptedException {
		final int m = 0;
		Chopstick[] cs = new Chopstick[5];
		for(int i = 0;i < 5;++i)
			cs[i] = new Chopstick();
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i = 0;i < 4;++i)
			exec.execute(new Philosopher(i, m, cs[i], cs[(i+1) % 5]));
		exec.execute(new DiffPhilosopher(5, m, cs[4], cs[0]));
		
		TimeUnit.SECONDS.sleep(100);
		
		exec.shutdownNow();
	}

	public static void deadLockTest() throws InterruptedException {
		final int m = 0;
		Chopstick[] cs = new Chopstick[5];
		for(int i = 0;i < 5;++i)
			cs[i] = new Chopstick();
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i = 0;i < 5;++i) 
			exec.execute(new Philosopher(i, m, cs[i], cs[(i+1) % 5]));
		
		TimeUnit.SECONDS.sleep(100);
		
		exec.shutdownNow();
	}

}
