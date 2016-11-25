package com.luoj.android.util.broadcast;

import android.content.BroadcastReceiver;

/**
 * @author LuoJ
 * @date 2014-12-25
 * @package j.android.library.receiver -- BaseBroadcastReceiver.java
 * @Description 广播基类
 */
public abstract class BaseBroadcastReceiver extends BroadcastReceiver {

	protected String action;
	
	protected abstract String setActionString();

	public BaseBroadcastReceiver() {
		super();
		action=setActionString();
	}
	
	public String getAction(){return action;}

}


