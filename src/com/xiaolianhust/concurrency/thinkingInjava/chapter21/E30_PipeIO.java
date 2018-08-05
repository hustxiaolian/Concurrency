package com.xiaolianhust.concurrency.thinkingInjava.chapter21;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class Sender implements Runnable{
	private PipedWriter pipeWriter = new PipedWriter();
	private Random rand = new Random();
	
	public PipedWriter getPipeWriter() {
		return pipeWriter;
	}

	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				for(char c= 'A';c <= 'z';++c) {
					pipeWriter.write((int)c);
					TimeUnit.MILLISECONDS.sleep(500 + rand.nextInt(1000));
				}
			}
		} catch (InterruptedException | IOException e) {
			System.out.println("interrupted or ioexception");
		}
	}
}

class Reader implements Runnable{
	private PipedReader pipeReader;

	public Reader(PipedWriter pipedWriter) throws IOException {
		super();
		this.pipeReader = new PipedReader(pipedWriter);
		
	}

	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				System.out.println("\tReader:" + (char)pipeReader.read());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}

class E30_Sender implements Runnable{
	private BlockingQueue<Character> charQueue;
	
	public E30_Sender(BlockingQueue<Character> charQueue) {
		super();
		this.charQueue = charQueue;
	}
	
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				for(char c = 'A';c <= 'z';++c) {
					charQueue.put(c);
					System.out.println("Writer:" + c);
					TimeUnit.MILLISECONDS.sleep(100);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}

class E30_Reader implements Runnable{
	private BlockingQueue<Character> charQueue;
	
	public E30_Reader(BlockingQueue<Character> charQueue) {
		super();
		this.charQueue = charQueue;
	}
	
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				System.out.println("Reader:" + charQueue.take());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}

public class E30_PipeIO {

	public static void main(String[] args) throws IOException, InterruptedException {
		BlockingQueue<Character> queue = new LinkedBlockingQueue<>();
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new E30_Sender(queue));
		exec.execute(new E30_Reader(queue));
		
		TimeUnit.SECONDS.sleep(5);
		
		exec.shutdownNow();
	}

	public static void test1() throws IOException, InterruptedException {
		Sender s = new Sender();
		Reader r = new Reader(s.getPipeWriter());
		
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(s);
		exec.execute(r);
		
		TimeUnit.SECONDS.sleep(5);
		
		exec.shutdownNow();
	}

}
