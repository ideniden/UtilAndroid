package com.luoj.android.util.thread.template;

import android.os.Handler;

import com.luoj.android.util.thread.BaseThread;
import com.luoj.android.util.thread.ThreadManager;

/**
 * @author LuoJ
 * @date 2014-1-2
 * @package j.android.library.base.thread.template -- TemplateThread.java
 * @Description 可中断线程 基类
 */
public abstract class AbsTemplateThread<T> extends BaseThread {
	
	protected Handler handler;// 数据传递者

	@Override
	protected void threadRun() {
		T result = longTimeOperate();
		
		if(!isCancel())handleResult(result);// 判断是否状态为取消，取消则直接结束线程
	}

	/**
	 * 此方法中执行耗时操作（例如网络获取数据、数据库查询数据等）
	 * @return 指定的返回类型
	 */
	protected abstract T longTimeOperate();

	/**
	 * 此方法中处理操作后返回的结果（T：指定的返回类型）
	 * @param result 返回的数据
	 */
	protected abstract void handleResult(T result);

	/**
	 * 是否截断线程（提前退出）
	 */
	protected boolean isCancel() {
		if (!isRun) {
			log("****手动结束线程："+getName()+"****");
			if(null!=handler)handler.sendEmptyMessage(ThreadManager.CANCEL_REQUEST);
			removeThreadInList();
			return true;
		}
		return false;
	}
}
