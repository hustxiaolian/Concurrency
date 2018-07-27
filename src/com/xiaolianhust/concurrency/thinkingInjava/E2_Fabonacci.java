package com.xiaolianhust.concurrency.thinkingInjava;

import java.util.Arrays;

public class E2_Fabonacci implements Runnable ,Generator<Integer>{
	public static int taskCount;
	public final int id = taskCount++;
	private final int n;
	private int count = 0;
	
	E2_Fabonacci(int n) {
		super();
		this.n = n;
	}
	
	public int fib(int n) {
		if(n < 2) return 1;
		return fib(n - 1) + fib(n - 2);
	}

	/**
	 * 这种方法也太慢了，上次那个logn的方法有点方法，这次先用线性方法
	 */
	public void run1() {
		Integer[] arr = new Integer[n];
		for(int i = 0;i < n;++i)
			arr[i] = fib(i);
		System.out.println("task[" + id + "] Seq. of n: " + Arrays.toString(arr));
	}

	@Override
	public Integer next() {
		return fib(count++);
	}
	
	public static void main(String[] args) {
		new Thread(new E2_Fabonacci(10)).start();
	}

	@Override
	public void run() {
		int[] arr = new int[n];
		for(int i = 0;i < Math.min(2, n);++i) {
			arr[i] = 1;
		}
		for(int i = 2;i < n;++i)
			arr[i] = arr[i - 1] + arr[i - 2];
		System.out.println("task[" + id + "] Seq. of n: " + Arrays.toString(arr));
	}
	
	
}
