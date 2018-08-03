package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.AsynchronousCloseException;
import java.sql.Time;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class SleepBlock implements Runnable{

	@Override
	public void run() {
		try {
			TimeUnit.SECONDS.sleep(100);
		} catch (InterruptedException e) {
			System.out.println("InterruptedException");
		}
		System.out.println("Exiting SleepBlock.run()");
	}
}

class IOBlocked implements Runnable{
	private InputStream in;
	
	public IOBlocked(InputStream in) {
		super();
		this.in = in;
	}

	@Override
	public void run() {
		try {
			System.out.println("Waiting for read():");
			in.read();
		} catch (IOException e) {
			if(Thread.currentThread().isInterrupted()) {
				System.out.println("Interrupted from blocked I/O");
			}else {
				throw new RuntimeException(e);
			}
		}
		System.out.println("Exit IOBloceked.run()");
	}
	
}

class SynchronizedBlocked implements Runnable{
	public synchronized void f() {
		while(true)
			Thread.yield();
	}
	
	public SynchronizedBlocked() {
		new Thread() {
			@Override
			public void run() {
				f();
			}
		}.start();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Trying to call f()...");
		f();
		System.out.println("Exiting SynchronizedBlocked.run()");
	}
	
}

public class Interrupting {
	private static ExecutorService exec = Executors.newCachedThreadPool();
	
	public static void test(Runnable r) throws InterruptedException {
		Future<?> f = exec.submit(r);
		TimeUnit.MILLISECONDS.sleep(100);
		System.out.println("Interrupting " + r.getClass().getSimpleName());
		f.cancel(true);
		System.out.println("Interrupt sent to " + r.getClass().getSimpleName());
		System.out.println("-----------------test done----------------");
	}
	
	public static void main(String[] args) throws InterruptedException {
		test(new SleepBlock());
		test(new IOBlocked(System.in));
		test(new SynchronizedBlocked());
		
		TimeUnit.SECONDS.sleep(3);
		
		System.out.println("system exit");
		System.exit(0);
	}
}
