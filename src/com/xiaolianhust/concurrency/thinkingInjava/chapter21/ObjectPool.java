package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

class Fat{
	private volatile double d;
	private static int counter = 0;
	private final int id = counter++;
	public Fat() {
		for(int i = 0;i < 250000;++i)
			d += (Math.PI + Math.E) / (double)i;
	}
	public void operate() {
		System.out.println(this);
	}
	@Override
	public String toString() {
		return "Fat id: " + id;
	}
}


class CheckOutTask<T> implements Runnable{
	private static int idCounter;
	private final int id = idCounter++;
	private ObjectPool<T> pool;

	public CheckOutTask(ObjectPool<T> pool) {
		super();
		this.pool = pool;
	}

	@Override
	public void run() {
		try {
			System.out.printf("%s: ���ڵȴ���ȡǩ֤....\n", this);
			T f = pool.checkOut();
			System.out.printf("%s: �õ�ǩ֤(%s)�����������۷�...\n", this, f);
			TimeUnit.SECONDS.sleep(1);
			System.out.printf("%s: OK, �����ˣ��黹ǩ֤(%s)...\n", this, f);
			pool.checkIn(f);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "CheckOutTask " + id;
	}
	
}

public class ObjectPool<T> {
	private List<T> items;
	private int size;
	private boolean[] unused;
	private Semaphore available;
	public ObjectPool(Class<T> cls, int size) {
		super();
		this.size = size;
		items = new ArrayList<>(size);
		unused = new boolean[size];
		Arrays.fill(unused, true);
		available = new Semaphore(size, true);
		
		try {
			for(int i = 0;i < size; ++i)
				items.add(cls.newInstance());//������ӵ��Ĭ�ϵ��޲ι��캯����
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public T checkOut() throws InterruptedException {
		available.acquire();
		return getItem();
	}

	public synchronized T getItem() {
		int index;
		for(index = 0;index < size; ++index) {
			if(unused[index]) {
				unused[index] = false;
				return items.get(index);
			}
		}
		return null;//��ʵ�Ҹо��������Ӧ�ò�����֡�
	}
	
	public boolean checkIn(T x) {
		available.release();
		return releaseItem(x);
	}

	public synchronized boolean releaseItem(T x) {
		int index = items.indexOf(x);
		if(index == -1) return false;//��ֹ���Լ�new��һ������Ȼ�������������
		if(!unused[index]) {
			unused[index] = true;
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) throws InterruptedException {
		final int size = 10;
		ObjectPool<Fat> pool = new ObjectPool<>(Fat.class, size);
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i = 0;i < 15;++i) {
			exec.execute(new CheckOutTask<Fat>(pool));
		}
		
		TimeUnit.SECONDS.sleep(5);
		exec.shutdownNow();
	}
	
	
}
