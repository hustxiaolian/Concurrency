package com.xiaolianhust.concurrency.thinkingInjava;

import java.io.IOException;

public class ResponsiveUI extends Thread{
	static volatile double d = 1.0;
	public ResponsiveUI() {
		start();
	}
	
	@Override
	public void run() {
		while(true) {
			d = d + (Math.PI + Math.E) / d;
		}
	}

	public static void main(String[] args) throws IOException {
		new ResponsiveUI();
		System.in.read();
		System.out.println(d);
	}

}
