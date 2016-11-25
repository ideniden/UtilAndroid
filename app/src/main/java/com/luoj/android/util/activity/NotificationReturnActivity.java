package com.luoj.android.util.activity;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author LuoJ
 * @date 2015-1-5
 * @package j.android.library.base.activity -- NotificationReturnActivity.java
 * @Description 作为通过点击通知栏回到应用的一个跳板（目前的解决方案）
 */
public class NotificationReturnActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		finish();
	}
	
}


