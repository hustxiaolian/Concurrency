package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class DelayTask implements Runnable, Delayed{
	private static int idCounter = 0;
	private final int id = idCounter++;
	private final int delta;
	private final long trigger;
	protected static List<DelayTask> sequence = new ArrayList<>();
	
	public DelayTask(int delayMillisecond) {
		this.delta = delayMillisecond;
		trigger = System.nanoTime() + TimeUnit.NANOSECONDS.convert(delta, TimeUnit.MILLISECONDS);
		sequence.add(this);
	}
	
	@Override
	public void run() {
		System.out.println(this + " ");
	}

	@Override
	public int compareTo(Delayed arg0) {
		DelayTask that = (DelayTask) arg0;
		if(trigger < that.trigger) return -1;
		else if(trigger > that.trigger) return 1;
		else return 0;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return TimeUnit.NANOSECONDS.convert(trigger - System.nanoTime(), TimeUnit.NANOSECONDS);
	}

	@Override
	public String toString() {
		return String.format(" [%1$-4d]", delta) + "Task " + id;
	}
	
	
}

class DelayTaskRunner implements Runnable{
	private DelayQueue<DelayTask> q;
	
	public DelayTaskRunner(DelayQueue<DelayTask> q) {
		super();
		this.q = q;
	}

	@Override
	public void run() {
		try {
			while(!Thread.interrupted() || q.isEmpty()) {
				q.take().run();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}

public class DelayQueueDemo {

	public static void main(String[] args) throws InterruptedException {
		Random rand = new Random();
		ExecutorService exec = Executors.newCachedThreadPool();
		DelayQueue<DelayTask> queue = new DelayQueue<>();
		for(int i = 0;i < 20;++i) {
			queue.put(new DelayTask(rand.nextInt(5000)));
		}
		exec.execute(new DelayTaskRunner(queue));
		
		
		
	}

}
