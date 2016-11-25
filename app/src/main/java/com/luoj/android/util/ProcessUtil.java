package com.luoj.android.util;

import android.app.ActivityManager;
import android.content.Context;

/**
 * @author LuoJ
 * @date 2014-6-27
 * @package j.android.library.utils -- ProcessUtil.java
 * @Description 进程工具类
 */
public class ProcessUtil {

	/**
	 * 获取当前进程的名称.
	 * @param context
	 * @return
	 */
	public static String getCurProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}
}
