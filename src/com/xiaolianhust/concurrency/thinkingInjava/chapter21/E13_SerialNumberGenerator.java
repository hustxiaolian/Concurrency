package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

public class E13_SerialNumberGenerator {
	private static volatile int searialNumber = 1;
	public synchronized static int nextSearialNumber() {
		return searialNumber++;
	}
}
