package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class E15_CriticalSection {
	private Object synLock1 = new Object();
	private Object synLock2 = new Object();
	private Object synLock3 = new Object();
	public void f() {
		synchronized (synLock1) {
			for(int i = 0;i < 5;++i) {
				System.out.println("f()");
				Thread.yield();
			}
		}
	}
	
	public void g() {
		synchronized (synLock2) {
			for(int i = 0;i < 5;++i) {
				System.out.println("g()");
				Thread.yield();
			}
		}
	}
	
	public void m() {
		synchronized (synLock3) {
			for(int i = 0;i < 5;++i) {
				System.out.println("m()");
				Thread.yield();
			}
		}
	}
	
	static void test(E15_CriticalSection e) {
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new Runnable() {
			
			@Override
			public void run() {
				e.f();
			}
		});
		
		exec.execute(new Runnable() {
			
			@Override
			public void run() {
				e.g();
			}
		});
		
		exec.execute(new Runnable() {
			
			@Override
			public void run() {
				e.m();
			}
		});
	}
	
	public static void main(String[] args) {
		test(new E15_CriticalSection());
	}
}
