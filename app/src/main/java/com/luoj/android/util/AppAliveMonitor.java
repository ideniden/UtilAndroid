package com.luoj.android.util;

import android.app.Application;
import android.content.Intent;

import com.luoj.android.util.thread.template.AbsLoopThread;

/**
 * @author LuoJ
 * @date 2014-7-17
 * @package j.android.library.utils -- AppAliveListener.java
 * @Description 应用活动状态监控
 */
public class AppAliveMonitor {

	private Application application;
	private static AppAliveMonitor mAppAliveListener=null;
	public static AppAliveMonitor getInstance(Application application){
		if (null==mAppAliveListener) {
			mAppAliveListener=new AppAliveMonitor(application);
		}
		return mAppAliveListener;
	}
	private AppAliveMonitor(Application application) {
		this.application=application;
		loop.setDebug(true);
		loop.setSleepTime(DELAY);
	}

	public static final String ACTION_BROADCAST="action.appalivelistener";
	
	private final int DELAY=2000;//监控延迟
	
	private AbsLoopThread<Void> loop=new AbsLoopThread<Void>() {
		@Override
		protected Void longTimeOperate() {
			boolean action = AppUtil.isAction(application);
			if (action) {
				LogUtil.d("当前应用活动中");
			}else{
				LogUtil.d("当前应用不在活动中");
			}
			Intent intent=new Intent(ACTION_BROADCAST);
			intent.putExtra("isAction", action);
			application.sendBroadcast(intent);
			return null;
		}
		@Override
		protected void handleResult(Void result) {}
	};
	
	public void start(){
		if(!isRunning())loop.startThread();
	}
	
	public void stop(){
		if(isRunning())loop.stopThread();
	}
	
	public boolean isRunning(){
		return loop.isRun();
	}
	
	public int getLoopCount(){
		return loop.getLoopCount();
	}
	
}


