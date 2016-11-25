package com.luoj.android.util.thread;

import j.android.library.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * @author LuoJ
 * @date 2013-10-11
 * @package j.android.library.base.thread -- UrlThreadManager.java
 * @Description 访问URL的线程的管理类
 */
public class ThreadManager {
	
	public static final int CANCEL_REQUEST = -8899111;// 表示主动取消
	
	private boolean isDebug=false;
	
	private static Map<String,IThreadHandle> threadList=new HashMap<String,IThreadHandle>();//线程集合
	
	private static ThreadManager utManager;
	
	private ThreadManager(){}
	
	public static ThreadManager getInstance(){
		if (null==utManager) {
			utManager=new ThreadManager();
		}
		return utManager;
	}
	
	/**
	 * 添加线程到集合中
	 * @param threadName
	 * @param thread
	 */
	public void add2ThreadList(String threadName,IThreadHandle thread){
		threadList.put(threadName,thread);
	}
	
	/**
	 * 从集合中移出线程
	 * @param threadName
	 */
	public void removeThreadInList(String threadName){
		if(null!=threadList.get(threadName))threadList.remove(threadName);
	}
	
	/**
	 * 终止当前所有正在请求网络的线程
	 */
	public void stopAll(){
		if (null!=threadList) {
			if (!threadList.isEmpty()&&threadList.size()>0) {
				LogUtil.d("停止所有网络请求");
				for (IThreadHandle thread : threadList.values()) {
					thread.stopThread();
				}
				threadList.clear();
			}
		}
	}
	
	/**
	 * 返回正在执行的线程数量
	 * @return
	 */
	public int getThreadCount(){
		return threadList.size();
	}

	public boolean isDebug() {
		return isDebug;
	}

	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
		LogUtil.controllCustomFlag("BaseThread", LogUtil.D, isDebug);
	}
	
}
