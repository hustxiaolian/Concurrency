package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GreenHouseScheduler {
	private volatile boolean light = false;
	private volatile boolean water = false;
	private String thermostat = "Day ";
	
	/**
	 * 回头想一下，这两个函数为什么要加同步控制
	 * @return
	 */
	public synchronized String getThermostat() {
		return thermostat;
	}
	
	public synchronized void setThermostat(String thermostat) {
		this.thermostat = thermostat;
	}
	
	ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(10);
	
	public void schedule(Runnable event, long delay) {
		scheduler.schedule(event, delay, TimeUnit.MILLISECONDS);
	}
	
	public void repeat(Runnable event, long initialDelay, long period) {
		scheduler.scheduleAtFixedRate(event, initialDelay, period, TimeUnit.MILLISECONDS);
	}
	
	class LightOn implements Runnable{
		@Override
		public void run() {
			System.out.println("开灯...");
			light = true;//这里由volatile关键字来保证同步控制，因为这里只有最简单的赋值操作。它是原子性的。
		}
	}
	
	class LightOff implements Runnable{
		@Override
		public void run() {
			System.out.println("关灯...");
			light = false;
		}
	}
	
	class WaterOn implements Runnable{
		@Override
		public void run() {
			System.out.println("注水...");
			water = true;
		}
	}
	
	class WaterOff implements Runnable{
		@Override
		public void run() {
			System.out.println("关水...");
			water = true;
		}
	}
	
	class ThermostatDay implements Runnable{
		@Override
		public void run() {
			System.out.println("恒温器:日间模式...");
			setThermostat("Day ");
		}
	}
	
	class ThermostatNight implements Runnable{
		@Override
		public void run() {
			System.out.println("恒温器:夜间模式...");
			setThermostat("Night ");
		}
	}
	
	class Bell implements Runnable{
		@Override
		public void run() {
			System.out.println("Bing!");
		}
	}
	
	class Terminate implements Runnable{
		@Override
		public void run() {
			System.out.println("Terminating....");
			scheduler.shutdownNow();
			
			new Thread() {
				@Override
				public synchronized void start() {
					for(DataPoin d : data)
						System.out.println(d);
				}
			}.start();
		}
	}
	
	static class DataPoin{
		final Calendar time;
		final float temperature;
		final float humidity;
		
		public DataPoin(Calendar time, float temperature, float humidity) {
			super();
			this.time = time;
			this.temperature = temperature;
			this.humidity = humidity;
		}

		@Override
		public String toString() {
			return String.format("temperature: %1$.1f humidity: %2$.2f", temperature, humidity);
		}
	}
	
	private Calendar lastTime = Calendar.getInstance();
	{
		lastTime.set(Calendar.MINUTE, 30);
		lastTime.set(Calendar.SECOND, 60);
	}
	
	private float lastTemp = 65.0f;
	private int tempDirection = +1;
	private float lastHumidity = 50.0f;
	private int humidityDirection = +1;
	private Random rand = new Random();
	List<DataPoin> data = Collections.synchronizedList(new ArrayList<>());

	class CollectData implements Runnable{

		@Override
		public void run() {
			System.out.println("Collection Data");
			synchronized (GreenHouseScheduler.this) {
				lastTime.set(Calendar.MINUTE, lastTime.get(Calendar.MINUTE) + 30);
				if(rand.nextInt(5) == 4)
					tempDirection = -tempDirection;
				lastTemp = lastTemp + tempDirection * (1.0f * rand.nextFloat());
				if(rand.nextInt(5) == 4)
					humidityDirection = -humidityDirection;
				lastHumidity = lastHumidity + humidityDirection * rand.nextFloat();
				data.add(new DataPoin((Calendar) lastTime.clone(), lastTemp, lastHumidity));
			}
		}
		
	}
	
	public static void main(String[] args) {
		GreenHouseScheduler gh = new GreenHouseScheduler();
		gh.schedule(gh.new Terminate(), 5000);
		gh.repeat(gh.new Bell(), 0, 1000);
		gh.repeat(gh.new ThermostatNight(), 0, 2000);
		gh.repeat(gh.new LightOn(), 0, 200);
		gh.repeat(gh.new LightOff(), 0, 400);
		gh.repeat(gh.new WaterOn(), 0, 600);
		gh.repeat(gh.new WaterOff(), 0, 800);
		gh.repeat(gh.new ThermostatDay(), 0, 1400);
		gh.repeat(gh.new CollectData(), 500, 500);
	}
	
	
}
