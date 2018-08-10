package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Horse implements Runnable{
	private static int idCounter = 0;
	private static Random rand = new Random();
	private final int id = idCounter++;
	private int strides = 0;
	private CyclicBarrier barrier;

	public Horse(CyclicBarrier barrier) {
		super();
		this.barrier = barrier;
	}
	
	public synchronized int getStrides() {return strides;}
	
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				synchronized (this) {
					strides += rand.nextInt(3);
				}
				barrier.await();//当前轮次跑完了，等到打印
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public String toString() {
		return String.format("horse %d ", id);
	}
	
	public String tracks() {
		StringBuffer sb = new StringBuffer();
		for(int i = 0;i < getStrides();++i)
			sb.append("*");
		sb.append(id);
		return sb.toString();
	}
	
	
}

public class HorseRace {
	private final int FINISH_LINE = 75;
	private List<Horse> horses = new ArrayList<>();
	private ExecutorService exec = Executors.newCachedThreadPool();
	private CyclicBarrier barrier;
	
	public HorseRace(int nHorse, final int pause) {
		barrier = new CyclicBarrier(nHorse, new Runnable() {
			@Override
			public void run() {
				StringBuffer sb = new StringBuffer();
				for(int i = 0;i < FINISH_LINE;++i)
					sb.append("=");
				System.out.println(sb);
				for(Horse horse : horses)
					System.out.println(horse.tracks());
				for(Horse horse : horses) {
					if(horse.getStrides() > FINISH_LINE) {
						System.out.println(horse + "won!");
						exec.shutdownNow();
						return;
					}
				}
				try {
					TimeUnit.MILLISECONDS.sleep(pause);
				} catch (InterruptedException e) {
					// TODO: handle exception
				}
			}
		});
		
		for(int i = 0;i < nHorse;++i) {
			Horse horse = new Horse(barrier);
			horses.add(horse);
			exec.execute(horse);
		}
		
	}
	
	public static void main(String[] args) {
		new HorseRace(7, 2000);
	}

}
