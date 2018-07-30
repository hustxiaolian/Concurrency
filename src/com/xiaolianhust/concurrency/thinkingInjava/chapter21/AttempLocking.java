package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AttempLocking {
	private Lock lock = new ReentrantLock();
	/**
	 * �������ֵĹ��÷��úúü�ס��������
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
	 * ��ʽ�϶���һ���ģ���lock()�����ĺ������try-fianlly��䣬Ȼ����finally������жϲ���unlock
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
