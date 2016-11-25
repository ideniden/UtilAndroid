package com.luoj.android.util.thread.template;

import android.os.Handler;

import com.luoj.android.util.thread.BaseThread;
import com.luoj.android.util.thread.ThreadManager;

/**
 * @author LuoJ
 * @date 2014-2-11
 * @package j.android.library.base.thread.template -- AbsLoopThread.java
 * @Description 循环执行线程
 */
public abstract class AbsLoopThread<T> extends BaseThread {
	
	private int loopCount=0;//执行次数
	
	private long sleepTime=0;//间隔时间
	
	private long startTime=0;//开始时间
	
	private long StopTime=0;//结束时间
	
	private int timerMax=-1;
	
	protected Handler handler;// 数据传递者

	protected AbsLoopThread(){}
	
	protected AbsLoopThread(long sleepTime){
		this.sleepTime=sleepTime;
	}
	
	private boolean initialized=false;
	
	@Override
	protected void threadRun() {
		++loopCount;
		
		if(!initialized){
			removeThreadInList();//从线程集合中去除，不受CustomThreadManager的管理,防止CustomThreadManager.stopAll()。
			initialized=true;
//			log(LogLevel.D, "开始循环...");
			startTime=System.currentTimeMillis();
			onStart();
		}
		
		T result = longTimeOperate();
		
		if (timerMax!=-1)timer();
		
		if(!isCancel())handleResult(result);// 判断是否状态为取消，取消则直接结束线程
		
		try {
			sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(!isCancel()){
			threadRun();
		}else{
			onStop();
		}
	}
	
	/**
	 * 计时器
	 */
	private void timer() {
		if ((timerMax-loopCount)==0) {
			stopThread();
		}
	}

	/**
	 * 获取剩余次数
	 * @return
	 */
	public int getResidueCount(){
		return (timerMax-loopCount);
	}
	
	protected void onStart(){}
	protected void onStop(){}
	
	@Override
	public void stopThread() {
		super.stopThread();
		StopTime=System.currentTimeMillis();
	}
	
	/**
	 * 获取运行时长
	 * @return
	 */
	public long getRunningTime(){
		long curTime=System.currentTimeMillis();
		long runningTime=curTime-startTime;
		if (startTime==0) {
			log("线程还没有执行...");
			return 0;
		}
		return runningTime;
	}
	
	/**
	 * 获取设置的循环间隔
	 * @return
	 */
	public long getSleepTime() {
		return sleepTime;
	}
	
	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}

	/**
	 * 获取已经循环的次数
	 * @return
	 */
	public int getLoopCount() {
		return loopCount;
	}

	/**
	 * 获取开始时间.
	 * @return
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * 获取停止时间.
	 * @return
	 */
	public long getStopTime() {
		return StopTime;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	/**
	 * 此方法中执行耗时操作（例如网络获取数据、数据库查询数据等）.
	 * @return 指定的返回类型
	 */
	protected abstract T longTimeOperate();

	/**
	 * 此方法中处理操作后返回的结果（T：指定的返回类型）.
	 * @param result 返回的数据
	 */
	protected abstract void handleResult(T result);

	/**
	 * 是否截断线程（提前退出）.
	 */
	protected boolean isCancel() {
		if (!isRun) {
			log("****手动结束线程："+getName()+"****");
			if(null!=handler)handler.sendEmptyMessage(ThreadManager.CANCEL_REQUEST);
			return true;
		}
		return false;
	}
	
	public void setTimer(int endCount){
		setTimer(endCount, 1000);
	}
	
	public void setTimer(int endCount,long delay){
		this.timerMax=endCount;
		this.sleepTime=delay;
	}
	
}


