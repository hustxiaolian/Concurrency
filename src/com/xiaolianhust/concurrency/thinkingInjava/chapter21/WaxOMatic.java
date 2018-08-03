package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.sql.Time;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * ��ʾһ������
 * @author 25040
 *
 */
class Car{
	private boolean waxOn = false;//��ʾ�����Ƿ����
	
	/**
	 * ��ʾ�Ѿ���ɴ�����֪ͨ����
	 */
	public synchronized void waxed() {
		waxOn = true;
		this.notifyAll();
	}
	/**
	 * ��ʾ�Ѿ�����׹⣬֪ͨ��
	 */
	public synchronized void buffed() {
		waxOn = false;
		this.notifyAll();
	}
	/**
	 * �ȴ�������׹����񣬼��ȴ�����ʱ����δ�׹⣬Ҳ����true
	 * @throws InterruptedException 
	 */
	public synchronized void waitForBuffed() throws InterruptedException {
		while(waxOn == true) {
			wait();
		}
	}
	/**
	 * �ȴ�����ɴ���������ˣ�ѭ������ΪwaxOnΪ���ڴ���,��δ��ɴ�����fasle
	 * @throws InterruptedException 
	 */
	public synchronized void waitForWaxed() throws InterruptedException {
		while(waxOn == false) {
			wait();
		}
	}
}

class WaxTask implements Runnable{
	private Car car;
	
	public WaxTask(Car car) {
		super();
		this.car = car;
	}

	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				System.out.println(this + "���ڴ���");
				TimeUnit.SECONDS.sleep(2);
				System.out.println(this + "������ɣ�֪ͨ�³�");
				car.waxed();
				System.out.println(this + "�ȴ��׹��������");
				car.waitForBuffed();
			}
		} catch (InterruptedException e) {
			System.out.println("WaxTask exit via interruped...");
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": ";
	}
	
	
}

class BuffTask implements Runnable{
	private Car car;
	public BuffTask(Car car) {
		super();
		this.car = car;
	}
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				System.out.println(this + "�ȴ�����ɴ���");
				car.waitForWaxed();
				System.out.println(this + "��ʼ�׹�");
				TimeUnit.SECONDS.sleep(2);
				System.out.println(this + "����׹⣬֪ͨ��");
				car.buffed();
			}
		} catch (InterruptedException e) {
			System.out.println("BuffTask exit via interruped...");
		}
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + ": ";
	}
	
}

public class WaxOMatic {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newCachedThreadPool();
		Car c = new Car();
		exec.execute(new WaxTask(c));
		exec.execute(new BuffTask(c));
		
		TimeUnit.SECONDS.sleep(50);
		exec.shutdownNow();
		TimeUnit.SECONDS.sleep(1);
		System.out.println("system done!exit!");
		System.exit(0);
	}

}
