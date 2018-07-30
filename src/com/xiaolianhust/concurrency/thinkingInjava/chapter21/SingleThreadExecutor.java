package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadExecutor {
	public static void main(String[] args) {
		ExecutorService exec = Executors.newSingleThreadExecutor();
		for(int i = 0;i < 5;++i) {
			exec.execute(new LiftOff());
		}
		System.out.println("shutdown.");//���߳�ִ�����������Ŷ�һ��ִ���ύ���߳�����
		exec.shutdown();
	}
}
