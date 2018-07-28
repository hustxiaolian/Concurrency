package com.xiaolianhust.concurrency.thinkingInjava;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class E10_RunTaskFabonacci {
	public static Callable<String> getCallable(final int n){
		return new Callable<String>() {
			@Override
			public String call() throws Exception {
				int[] arr = new int[n];
				for(int i = 0;i < Math.min(2, n);++i)
					arr[i] = 1;
				for(int i = 2;i < n;++i)
					arr[i] = arr[i - 1] + arr[i - 2];
				return Arrays.toString(arr);
			}
		};
	}
	
	public static Future<String> runTask(final int n){
		ExecutorService exec = Executors.newSingleThreadExecutor();
		return exec.submit(getCallable(n));
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		System.out.println(runTask(10).get());
		
	}
}	
