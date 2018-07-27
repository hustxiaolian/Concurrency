package com.xiaolianhust.concurrency.thinkingInjava;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedThreadPool {
	public static void main(String[] args) {
		ExecutorService exec = Executors.newFixedThreadPool(5);
		for(int i = 0;i < 5;++i) {
			exec.execute(new LiftOff());
		}
		System.out.println("shutdown.");//Ҳ����˵��һ�����ȰѸ��̴߳����ã�Ȼ��shutdown��һ��ִ��
		exec.shutdown();
	}
}
