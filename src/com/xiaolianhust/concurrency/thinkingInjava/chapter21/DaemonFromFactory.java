package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DaemonFromFactory implements Runnable {

	@Override
	public void run() {
		try {
			TimeUnit.MILLISECONDS.sleep(100);
			System.out.println(Thread.currentThread() + " " + this);
		} catch (Exception e) {
			System.out.println("Interrupt!");
		}
	}
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool(new DaemonThreadFactory());
		//���ﴫ�ݹ�ȥ��ThreadFactory����������Ӧ��������ĳ�����ģʽ�����ڴ����������������������еĴ��ݵ�task���ᱻ���óɺ�̨�̵߳õ�ִ��
		for(int i = 0;i < 10;++i) {
			exec.execute(new DaemonFromFactory());
		}
		
		System.out.println("all start!");
		try {
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
