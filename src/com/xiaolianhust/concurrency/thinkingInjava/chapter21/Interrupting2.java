package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BlockMutex{
	private Lock lock = new ReentrantLock();
	
	public BlockMutex() {
		lock.lock();
	}
	
	public void f() {
		try {
			lock.lockInterruptibly();
			System.out.println("hello, never arrive here");
		} catch (InterruptedException e) {
			System.out.println("BlockMutex.f() interruped...'");
		}
	}
}

class Block2 implements Runnable{
	private BlockMutex block = new BlockMutex();
	@Override
	public void run() {
		System.out.println("waiting for f()...");
		block.f();
		System.out.println("BlockMutex.f() exit...");
	}
	
}

public class Interrupting2 {

	public static void main(String[] args) throws InterruptedException {
		Thread t = new Thread(new Block2());
		t.start();
		TimeUnit.SECONDS.sleep(5);
		t.interrupt();
	}

}
