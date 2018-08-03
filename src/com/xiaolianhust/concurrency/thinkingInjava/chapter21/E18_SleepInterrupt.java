package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class E18_NoTask {
	public void f() throws InterruptedException {
		System.out.println("NoTask.f()...");
		TimeUnit.SECONDS.sleep(1);
		System.out.println("f() done!");
	}
}

class E18_Task implements Runnable{
	private E18_NoTask t = new E18_NoTask();
	public boolean isRun = true;
	@Override
	public void run() {
		try {
			while(true) {
				t.f();
			}
		} catch (InterruptedException e) {
			System.out.println("Notask.f() interrupted...");
		}finally {
			System.out.println("cleaning...");
		}
	}
	
}

public class E18_SleepInterrupt {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.execute(new E18_Task());
		
		
		TimeUnit.SECONDS.sleep(5);
		exec.shutdownNow();
		TimeUnit.SECONDS.sleep(2);
		System.exit(0);
	}

}
