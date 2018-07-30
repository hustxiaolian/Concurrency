package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

public class E11_SynchronizedTest implements Runnable{
	
	private int i = 0;
	private int j = 100;
	
	private synchronized void sumIJ() {
		System.out.println(Thread.currentThread() + " sum i and j: " + (i++ + j--));
	}

	@Override
	public void run() {
		while(true)
			sumIJ();
	}
	
	public static void main(String[] args) {
		SynchronizedTestUtil.test(new E11_SynchronizedTest(), 5);
	}

}
