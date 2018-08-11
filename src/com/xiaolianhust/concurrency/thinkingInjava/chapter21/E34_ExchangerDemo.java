package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class ExchangeProducer<T> implements Runnable{
	private Exchanger<List<T>> exchanger;
	private List<T> holder;
	private Generator<T> gen;
	
	public ExchangeProducer(Exchanger<List<T>> exchanger, List<T> holder, Generator<T> gen) {
		super();
		this.exchanger = exchanger;
		this.holder = holder;
		this.gen = gen;
	}
	
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				for(int i = 0;i < 10;++i) {
					T x = gen.next();
					TimeUnit.SECONDS.sleep(1);
					System.out.println(this + ":" + "生产了" + x);
					holder.add(x);
				}
				System.out.println(this + "生产完毕，等待交换...");
				holder = exchanger.exchange(holder);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public String toString() {
		return "Producer";
	}	
	
	
}

class ExchangeConsumer<T> implements Runnable{
	private Exchanger<List<T>> exchanger;
	private List<T> holder;
	
	public ExchangeConsumer(Exchanger<List<T>> exchanger, List<T> holder) {
		super();
		this.exchanger = exchanger;
		this.holder = holder;
	}

	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				System.out.println(this + ":消费完毕，等待交换...");
				holder = exchanger.exchange(holder);
				
				for(int i = 9;i >= 0;--i) {
					T x = holder.remove(i);
					TimeUnit.SECONDS.sleep(2);
					System.out.println(this + ":消费了" + x);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public String toString() {
		return "Comsumer:";
	}
}

class Middle{
	private static int idCounter;
	private final int id = idCounter++;
	@Override
	public String toString() {
		return "Middle [" + id + "]";
	}
	
	public static Middle getMiddle() {
		return new Middle();
	}
}

public class E34_ExchangerDemo {

	public static void main(String[] args) throws InterruptedException {
		List<Middle> h1 = new ArrayList<>();
		List<Middle> h2 = new ArrayList<>();
		Exchanger<List<Middle>> exchanger = new Exchanger<>();
		ExchangeProducer<Middle> p = new ExchangeProducer<>(exchanger, h1,
				new Generator<Middle>() {
			@Override
			public Middle next() {
				return Middle.getMiddle();
			}
		});
		
		ExchangeConsumer<Middle> c = new ExchangeConsumer<>(exchanger, h2);
		
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(p);
		exec.execute(c);
		
		TimeUnit.SECONDS.sleep(100);
		
		exec.shutdownNow();
	}

}
