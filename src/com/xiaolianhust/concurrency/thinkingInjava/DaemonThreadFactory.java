package com.xiaolianhust.concurrency.thinkingInjava;

import java.util.concurrent.ThreadFactory;

public class DaemonThreadFactory implements ThreadFactory {

	@Override
	public Thread newThread(Runnable arg0) {
		Thread t = new Thread(arg0);
		t.setDaemon(true);//设置为后台线程
		return t;
	}

}
