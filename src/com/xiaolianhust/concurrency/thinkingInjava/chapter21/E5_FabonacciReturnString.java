package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class E5_FabonacciReturnString implements Callable<String> {
	private int n;
	
	
	E5_FabonacciReturnString(int n) {
		super();
		this.n = n;
	}

	@Override
	public String call() throws Exception {
		int[] arr = new int[n];
		for(int i = 0;i < Math.min(2, n);++i) {
			arr[i] = 1;
		}
		for(int i = 2;i < n;++i)
			arr[i] = arr[i - 1] + arr[i - 2];
		return Arrays.toString(arr);
	}

	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future<String>> result = new ArrayList<>();
		for(int i = 0;i < 10;++i) {
			result.add(exec.submit(new E5_FabonacciReturnString(i)));
		}
		
		for(Future<String> fs : result) {
			try {
				System.out.println(fs.get());
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

}
