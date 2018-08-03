package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Pair {
	private int x, y;

	public Pair(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void incrementX() {
		++x;
	}
	
	public void incrementY() {
		++y;
	}

	@Override
	public String toString() {
		return "Pair [x=" + x + ", y=" + y + "]";
	}
	
	public void checkState() {
		if(x != y)
			throw new RuntimeException("x not equal y:" + x + "," + y);
	}
}

abstract class PairManager{
	AtomicInteger checkCounter = new AtomicInteger(0);
	protected Pair p = new Pair(0, 0);
	private List<Pair> storage = java.util.Collections.synchronizedList(new ArrayList<>());
	
	public synchronized Pair getPair() {
		return new Pair(p.getX(), p.getY());
	}
	
	protected void store(Pair p) {
		storage.add(p);
		try {
			TimeUnit.MILLISECONDS.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public abstract void incrementAndStore();
}

class PairManager1 extends PairManager{

	@Override
	public synchronized void incrementAndStore() {
		p.incrementX();
		p.incrementY();
		store(getPair());
	}
	
}

class PairManager2 extends PairManager{

	@Override
	public void incrementAndStore() {
		Pair temp;
		synchronized (this) {
			p.incrementX();
			p.incrementY();
			temp = getPair();
		}
		store(temp);
		//这里由于我们的list本身就是同步机制保护了，所以可以放在外面，就是这点差距，就可以在多线程中获取更高的性能
	}
}

class PairManager3 extends PairManager{
	private Lock lock = new ReentrantLock();
	
	@Override
	public void incrementAndStore() {
		Pair temp;
		lock.lock();
		try {
			p.incrementX();
			p.incrementY();
			temp = getPair();
		} finally {
			lock.unlock();
		}
		store(temp);
	}
	
}

class PairIncrementor implements Runnable{
	private PairManager pm;
	
	public PairIncrementor(PairManager pm) {
		super();
		this.pm = pm;
	}
	
	@Override
	public void run() {
		while(true)
			pm.incrementAndStore();
	}
	
	public String toString() {
		return "Pair: " + pm.getPair() + " checkConuter = " + pm.checkCounter.get();
	}
}

class PairChecker implements Runnable{
	private PairManager pm;
	
	public PairChecker(PairManager pm) {
		super();
		this.pm = pm;
	}

	@Override
	public void run() {
		while(true) {
			pm.checkCounter.addAndGet(1);
			pm.getPair().checkState();
		}
	}
	
}

public class CriticalSection {
	/*
	public static void test(PairManager pm1, PairManager pm2) {
		ExecutorService exec = Executors.newCachedThreadPool();
		
		PairIncrementor pi1 = new PairIncrementor(pm1);
		PairIncrementor pi2 = new PairIncrementor(pm2);
		
		PairChecker pc1 = new PairChecker(pm1);
		PairChecker pc2 = new PairChecker(pm2);
		
		exec.execute(pi1);
		exec.execute(pi2);
		exec.execute(pc1);
		exec.execute(pc2);
		
		try {
			TimeUnit.MILLISECONDS.sleep(1000);
		} catch (InterruptedException e) {
			System.out.println("interrupted...");
		}
		
		System.out.println(pi1);
		System.out.println(pi2);
		
		System.out.println();
	}
	*/
	public static void test(PairManager...pms) {
		int n = pms.length;
		ExecutorService exec = Executors.newCachedThreadPool();
		PairIncrementor[] pis = new PairIncrementor[n];
		PairChecker[] pcs = new PairChecker[n];
		
		for(int i = 0;i < n;++i) {
			pis[i] = new PairIncrementor(pms[i]);
			pcs[i] = new PairChecker(pms[i]);
		}
		
		for(int i = 0;i < n;++i) {
			exec.execute(pis[i]);
			exec.execute(pcs[i]);
		}
		
		exec.shutdown();
		
		try {
			TimeUnit.MILLISECONDS.sleep(1000);
		} catch (InterruptedException e) {
			System.out.println("Interrupted...");
		}
		
		for(int i = 0;i < n;++i) {
			System.out.println(pis[i]);
		}
		System.exit(0);
	}
	
	public static void main(String[] args) {
		test(new PairManager1(), new PairManager2(), new PairManager3());
	}
	
}
