package com.xiaolianhust.concurrency.thinkingInjava;

import java.util.concurrent.ThreadFactory;

public class DaemonThreadFactory implements ThreadFactory {

	@Override
	public Thread newThread(Runnable arg0) {
		Thread t = new Thread(arg0);
		t.setDaemon(true);//����Ϊ��̨�߳�
		return t;
	}

}
