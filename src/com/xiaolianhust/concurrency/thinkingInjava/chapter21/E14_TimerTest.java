package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.Timer;
import java.util.TimerTask;

public class E14_TimerTest{
	public static void main(String[] args) {
		Timer timer = new Timer(false);
		
		for(int i = 0;i < 1000;++i) {
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					System.out.println(this + ": hello,xiaolian");
				}
			}, 20000);
		}
	}
}
