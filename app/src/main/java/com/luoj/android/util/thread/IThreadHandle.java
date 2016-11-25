package com.luoj.android.util.thread;

import java.util.concurrent.CountDownLatch;

/**
 * @author LuoJ
 * @date 2013-9-24
 * @package j.android.library.base.thread -- IUrlThread.java
 * @Description 线程的行为的定义
 */
public interface IThreadHandle {
	
	/**
	 * 开启线程.
	 */
	public void startThread();

	/**
	 * 停止当前线程（安全提前退出）.
	 */
	public void stopThread();
	
	/**
	 * 是否打印log信息（级别在manager.debug之上）.
	 * @return
	 */
	public boolean isDebug();

	/**
	 * 
	 * @param isDebug.
	 */
	public void setDebug(boolean isDebug);
	
	/**
	 * 
	 * @param signal
	 */
	public void setSignal(CountDownLatch signal);
	
}
