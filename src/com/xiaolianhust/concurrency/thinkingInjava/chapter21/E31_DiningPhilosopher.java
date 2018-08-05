package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 使用阻塞队列完成，阻塞队列也就是筷笼，
 * 哲学家既是消费者，也是生产者。
 * 如果哲学家拿筷子的时候，如果是从筷笼中一根根的取，那么有产生死锁的可能性，
 * 如果使用同步控制，那么保证每次当筷笼的剩余的筷子数目大于1的时候，哲学家一次性拿出两个。
 * 
 * @author 25040
 *
 */
class E31_Chopstick{}

@SuppressWarnings("serial")
class E31_ChopstickBox extends LinkedBlockingQueue<E31_Chopstick> {}

class E31_Philosopher implements Runnable{
	private E31_ChopstickBox box;
	private final int id;
	private Random rand = new Random();
	private E31_Chopstick left = null;
	private E31_Chopstick right = null;
	private final int ponderFactor;
	
	public E31_Philosopher(E31_ChopstickBox box, int id, int ponderFactor) {
		super();
		this.box = box;
		this.id = id;
		this.ponderFactor = ponderFactor;
	}

	public void pause() throws InterruptedException {
		if(ponderFactor == 0) return;
		TimeUnit.MILLISECONDS.sleep(rand.nextInt(ponderFactor * 250));
	}
	
	/**
	 * 如果使用这种方式，如果刚好每个哲学家同时出手，那么就会造成死锁。
	 * @throws InterruptedException
	 */
	public void take() throws InterruptedException {
		left = box.take();
		System.out.println(this + "拿起了左边的筷子");
		right = box.take();
		System.out.println(this + "拿起了右边的筷子");
	}
	
	public void drop() throws InterruptedException {
		box.put(right);
		right = null;
		System.out.println(this + "放下左边的筷子");
		box.put(left);
		left = null;
		System.out.println(this + "放下了右边的筷子");
	}
	
	/**
	 * 非死锁版本，这里加深了我对wait()方法的理解。
	 * 在当前线程中，可以对任意对象调用wait()方法（再次强调：必须先synchronized(obj)，才能在同步块内部调用obj.wait()）
	 * 它的意思说：它现在因为某些原因（这个原因与obj相关）没法运行下去，我先暂时停下，等待别的大佬改变这个obj的状态，
	 * 然后通知唤醒我。
	 * @throws InterruptedException
	 */
	public void takeTwo() throws InterruptedException {
		synchronized (box) {
			System.out.println(this + "检查筷笼中剩余筷子的数目");
			while(box.size() <= 1)
				box.wait();
			left = box.take();
			System.out.println(this + "拿起了左边的筷子");
			right = box.take();
			System.out.println(this + "拿起了右边的筷子");
		}
	}
	
	public void dropTwo() throws InterruptedException {
		synchronized (box) {
			box.put(right);
			right = null;
			System.out.println(this + "放下左边的筷子");
			box.put(left);
			left = null;
			System.out.println(this + "放下了右边的筷子");
			box.notifyAll();
		}
	}
	
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				System.out.println(this + "正在思考...");
				pause();
				System.out.println(this + "饿了，准备拿筷子吃饭...");
				takeTwo();
				System.out.println(this + "正在吃饭...");
				pause();
				dropTwo();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void runDead() {
		try {
			while(!Thread.interrupted()) {
				System.out.println(this + "正在思考...");
				pause();
				System.out.println(this + "饿了，准备拿筷子吃饭...");
				take();
				System.out.println(this + "正在吃饭...");
				pause();
				drop();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public String toString() {
		return "哲学家#" + id + "# :";
	}
}

public class E31_DiningPhilosopher {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newCachedThreadPool();
		E31_ChopstickBox box = new E31_ChopstickBox();
		for(int i = 0;i < 5;++i)
			box.put(new E31_Chopstick());
		
		E31_Philosopher[] ps = new E31_Philosopher[5];
		for(int i = 0;i < 5;++i)
			ps[i] = new E31_Philosopher(box, i, 0);
		
		for(int i = 0;i < 5;++i)
			exec.execute(ps[i]);
		
		TimeUnit.SECONDS.sleep(100);
		
		exec.shutdownNow();
	}

}
