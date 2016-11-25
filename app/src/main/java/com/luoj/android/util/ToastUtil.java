package com.luoj.android.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @author LuoJ
 * @date 2013-10-9
 * @package j.android.library.utils -- ToastUtil.java
 * @Description 显示对话框
 */
public class ToastUtil {
	
	/**
	 * 弹出提示
	 * @param context
	 * @param msg
	 */
	public static void showToast(Context context, String msg) {
		showToast(context,msg,Toast.LENGTH_SHORT);
	}

    public static void showToast(Context context,int stringResId){
        showToast(context,context.getString(stringResId));
    }

	/**
	 * 弹出提示
	 * @param context
	 * @param msg
	 */
	public static void showToast(Context context, String msg,int duration) {
		Toast toast = Toast.makeText(context, msg, duration);
		if(null!=toast)toast.show();
	}
}
