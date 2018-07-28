package com.xiaolianhust.concurrency.thinkingInjava;

import java.util.concurrent.ThreadFactory;

public class E9_PriorityThreadFactory implements ThreadFactory {
	private int priority;
	
	public E9_PriorityThreadFactory(int priority) {
		super();
		if(priority > 10)
			this.priority = 10;
		else if(priority < 1)
			this.priority = 1;
		else
			this.priority = priority;
	}

	@Override
	public Thread newThread(Runnable arg0) {
		Thread t = new Thread(arg0);
		t.setPriority(priority);
		return t;
	}

}
