package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 利用循环数组，定义的一个简单的集合类。
 * @author 25040
 *
 * @param <T>
 */
class CircleArraySet{
	private int index;
	private final int size;
	private int[] arr;
	
	public CircleArraySet(int size) {
		super();
		this.index = 0;
		this.size = size;
		this.arr = new int[size];

	}
	
	public synchronized void add(int val) {
		arr[index] = val;
		index = ++index % size;
	}
	
	public synchronized boolean contain(int val) {
		for(int i = 0;i < size;++i) {
			if(arr[i] == val)
				return true;
		}
		return false;
	}
}

public class E13_SearialNumberChecker {
	private static ExecutorService exec = Executors.newCachedThreadPool();
	private static CircleArraySet set = new CircleArraySet(1000);
	
	static class SearialCheckerTask implements Runnable{
		@Override
		public void run() {
			while(true) {
				int temp = E13_SerialNumberGenerator.nextSearialNumber();
				if(set.contain(temp)) {
					System.out.println("重复：" + temp);
					System.exit(1);
				}
				else
					set.add(temp);
			}
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		for(int i = 0;i < 10;++i) {
			exec.execute(new SearialCheckerTask());
		}
		TimeUnit.SECONDS.sleep(3);
		System.exit(1);
	}
}
