package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class RadiationSumBroad{
	private int sum = 0;
	private ArrayList<Integer> seniors = new ArrayList<>();
	private boolean switcher = true;
	
	public boolean isSwitcher() {
		return switcher;
	}

	public void setSwitcher(boolean switcher) {
		this.switcher = switcher;
	}

	public RadiationSumBroad() {
		super();
		this.sum = 0;
	}
	
	public int addSenior(E17_Radiation oneSenior) {
		int ret = seniors.size();
		seniors.add(0);
		return ret;
	}
	
	public void updateSum(int seniorId, Integer newVal) {
		sum = sum - seniors.get(seniorId) + newVal;
		seniors.set(seniorId, newVal);
		
	}
	
	public int getSum() {
		return sum;
	}
}

public class E17_Radiation implements Runnable{
	private Random rand = new Random();
	private RadiationSumBroad broad;
	private int soniorId;
	private int currNum;
	public E17_Radiation(RadiationSumBroad broad) {
		super();
		this.broad = broad;
		soniorId = broad.addSenior(this);
		currNum = 0;
	}
	
	public void updateBroad() {
		currNum = getNewData();
		synchronized (broad) {
			broad.updateSum(soniorId, currNum);
			System.out.println("sonior#" + soniorId + " : " + currNum + 
					" broad total: "+ broad.getSum());
			try {
				TimeUnit.MILLISECONDS.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("interrupted...");
			}
		}
	}

	public int getNewData() {
		return rand.nextInt(100);
	}

	@Override
	public void run() {
		while(broad.isSwitcher()) {
			updateBroad();
		}	
	}
	
	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newCachedThreadPool();
		RadiationSumBroad broad = new RadiationSumBroad();
		ArrayList<E17_Radiation> arr = new ArrayList<>();
		
		for(int i = 0;i < 5;++i) {
			arr.add(new E17_Radiation(broad));
		}
		
		for(int i = 0;i < 5;++i) {
			exec.execute(arr.get(i));
		}
		
		
		try {
			TimeUnit.SECONDS.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		broad.setSwitcher(false);
		if(exec.awaitTermination(2000, TimeUnit.MILLISECONDS)) {
			System.out.println("no done");
			System.exit(1);
		}
		else {
			System.out.println("done");
			System.exit(0);
		}
		
		
	}

}
