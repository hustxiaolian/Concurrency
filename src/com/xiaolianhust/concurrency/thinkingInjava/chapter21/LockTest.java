package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {
	private int i =1;
	private Lock lock  = new ReentrantLock();
	
	public void next() {
		lock.lock();
		try {
			i++;
			i++;
			System.out.println(i);
		} finally {
			lock.unlock();
		}
	}
}
