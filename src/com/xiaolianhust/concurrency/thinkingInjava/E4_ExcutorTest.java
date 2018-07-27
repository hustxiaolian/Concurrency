package com.xiaolianhust.concurrency.thinkingInjava;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class E4_ExcutorTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testExcutor(Executors.newCachedThreadPool(), new E2_Fabonacci(5), 10);
		System.out.println("------------");//注意到每次执行的结构的不同
		testExcutor(Executors.newFixedThreadPool(3), new E2_Fabonacci(5), 10);
		testExcutor(Executors.newSingleThreadExecutor(), new E2_Fabonacci(10), 5);
	}
	
	public static void testExcutor(ExecutorService exec, Runnable task, int n) {
		for(int i = 0;i < n;++i) {
			exec.execute(task);
		}
		exec.shutdown();
	}

}
