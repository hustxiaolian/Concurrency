package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * ʹ������������ɣ���������Ҳ���ǿ�����
 * ��ѧ�Ҽ��������ߣ�Ҳ�������ߡ�
 * �����ѧ���ÿ��ӵ�ʱ������Ǵӿ�����һ������ȡ����ô�в��������Ŀ����ԣ�
 * ���ʹ��ͬ�����ƣ���ô��֤ÿ�ε�������ʣ��Ŀ�����Ŀ����1��ʱ����ѧ��һ�����ó�������
 * 
 * @author 25040
 *
 */
class E31_Chopstick{}

@SuppressWarnings("serial")
class E31_ChopstickBox extends LinkedBlockingQueue<E31_Chopstick> {}

class E31_Philosopher implements Runnable{
	private E31_ChopstickBox box;
	private final int id;
	private Random rand = new Random();
	private E31_Chopstick left = null;
	private E31_Chopstick right = null;
	private final int ponderFactor;
	
	public E31_Philosopher(E31_ChopstickBox box, int id, int ponderFactor) {
		super();
		this.box = box;
		this.id = id;
		this.ponderFactor = ponderFactor;
	}

	public void pause() throws InterruptedException {
		if(ponderFactor == 0) return;
		TimeUnit.MILLISECONDS.sleep(rand.nextInt(ponderFactor * 250));
	}
	
	/**
	 * ���ʹ�����ַ�ʽ������պ�ÿ����ѧ��ͬʱ���֣���ô�ͻ����������
	 * @throws InterruptedException
	 */
	public void take() throws InterruptedException {
		left = box.take();
		System.out.println(this + "��������ߵĿ���");
		right = box.take();
		System.out.println(this + "�������ұߵĿ���");
	}
	
	public void drop() throws InterruptedException {
		box.put(right);
		right = null;
		System.out.println(this + "������ߵĿ���");
		box.put(left);
		left = null;
		System.out.println(this + "�������ұߵĿ���");
	}
	
	/**
	 * �������汾������������Ҷ�wait()��������⡣
	 * �ڵ�ǰ�߳��У����Զ�����������wait()�������ٴ�ǿ����������synchronized(obj)��������ͬ�����ڲ�����obj.wait()��
	 * ������˼˵����������ΪĳЩԭ�����ԭ����obj��أ�û��������ȥ��������ʱͣ�£��ȴ���Ĵ��иı����obj��״̬��
	 * Ȼ��֪ͨ�����ҡ�
	 * @throws InterruptedException
	 */
	public void takeTwo() throws InterruptedException {
		synchronized (box) {
			System.out.println(this + "��������ʣ����ӵ���Ŀ");
			while(box.size() <= 1)
				box.wait();
			left = box.take();
			System.out.println(this + "��������ߵĿ���");
			right = box.take();
			System.out.println(this + "�������ұߵĿ���");
		}
	}
	
	public void dropTwo() throws InterruptedException {
		synchronized (box) {
			box.put(right);
			right = null;
			System.out.println(this + "������ߵĿ���");
			box.put(left);
			left = null;
			System.out.println(this + "�������ұߵĿ���");
			box.notifyAll();
		}
	}
	
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				System.out.println(this + "����˼��...");
				pause();
				System.out.println(this + "���ˣ�׼���ÿ��ӳԷ�...");
				takeTwo();
				System.out.println(this + "���ڳԷ�...");
				pause();
				dropTwo();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void runDead() {
		try {
			while(!Thread.interrupted()) {
				System.out.println(this + "����˼��...");
				pause();
				System.out.println(this + "���ˣ�׼���ÿ��ӳԷ�...");
				take();
				System.out.println(this + "���ڳԷ�...");
				pause();
				drop();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public String toString() {
		return "��ѧ��#" + id + "# :";
	}
}

public class E31_DiningPhilosopher {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newCachedThreadPool();
		E31_ChopstickBox box = new E31_ChopstickBox();
		for(int i = 0;i < 5;++i)
			box.put(new E31_Chopstick());
		
		E31_Philosopher[] ps = new E31_Philosopher[5];
		for(int i = 0;i < 5;++i)
			ps[i] = new E31_Philosopher(box, i, 0);
		
		for(int i = 0;i < 5;++i)
			exec.execute(ps[i]);
		
		TimeUnit.SECONDS.sleep(100);
		
		exec.shutdownNow();
	}

}
