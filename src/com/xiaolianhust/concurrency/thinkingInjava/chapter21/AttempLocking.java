package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AttempLocking {
	private Lock lock = new ReentrantLock();
	/**
	 * 这里体现的惯用法得好好记住熟练才行
	 */
	public void untimed() {
		boolean captured = lock.tryLock();
		try {
			System.out.println("trylock result:" + captured);
		} finally {
			if(captured)
				lock.unlock();
		}
	}
	
	/**
	 * 形式上都是一样的，在lock()方法的后面紧跟try-fianlly语句，然后，在finally语句中判断并且unlock
	 * 
	 */
	public void timed() {
		boolean captured = false;
		try {
			captured = lock.tryLock(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		try {
			System.out.println("timed tryLock result: " + captured);
		} finally {
			if(captured)
				lock.unlock();
		}
	}
}
