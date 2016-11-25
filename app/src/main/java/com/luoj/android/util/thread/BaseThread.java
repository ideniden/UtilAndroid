package com.luoj.android.util.thread;

import j.android.library.utils.LogUtil;

import java.util.concurrent.CountDownLatch;


/**
 * @author LuoJ
 * @date 2013-9-24
 * @package j.android.library.base.thread -- AbstractMyThread.java
 * @Description 自定义线程的基类，实现提前退出线程(在耗时操作为完成前截断)
 */
public abstract class BaseThread extends Thread implements IThreadHandle{
	
	protected final String logModuleName="BaseThread";
	
	protected boolean isDebug;
	
	protected boolean isRun=false;//线程是否在运行
	
	protected boolean initiativeStop=false;//是否主动调用停止(stopThread)
	
	protected CountDownLatch signal;//其他线程信号(实现调用线程等待此请求线程完成后继续执行)
	
	public void setSignal(CountDownLatch signal){
		this.signal=signal;
	}
	
	protected ThreadManager ctManager;//线程管理类
	
	protected BaseThread(){
		ctManager=ThreadManager.getInstance();
	}

	@Override
	public void run() {
		log("ThreadName-->"+getName());
		isRun=true;
		addThread2List();
		
		threadRun();
		
		removeThreadInList();
		isRun=false;
	}
	
	/**
	 * 子类必须实现的线程执行函数.
	 */
	protected abstract void threadRun();
	
	/**
	 * 添加当前线程到线程集合.
	 */
	protected void addThread2List(){
		ctManager.add2ThreadList(getName(), this);
	}
	
	/**
	 * 从线程集合中清除此线程.
	 */
	protected void removeThreadInList(){
		ctManager.removeThreadInList(getName());
	}
	
	@Override
	public void startThread() {
		start();
	}
	
	/**
	 * 终止线程（跳过对主线程的操作，提前退出）.
	 */
	@Override
	public void stopThread(){
		isRun=false;
		initiativeStop=true;
	}
	
	/**
	 * 线程是否在运行中
	 * @return
	 */
	public boolean isRun() {
		return isRun;
	}

	@Override
	public boolean isDebug() {
		return isDebug;
	}

	@Override
	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}

	/**
	 * 打印log信息.
	 * @param msg log信息
	 * @param tr 异常对象
	 */
	public void log(String msg,Throwable tr){
		if (!isDebug) {
			return;
		}
		LogUtil.d(msg, logModuleName);
	}
	
	/**
	 * 打印log信息.
	 * @param msg
	 */
	public void log(String msg){
		log(msg, null);
	}
}
