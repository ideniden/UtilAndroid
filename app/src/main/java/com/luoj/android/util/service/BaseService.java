package com.luoj.android.util.service;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.luoj.android.util.activity.NotificationReturnActivity;


/**
 * @author LuoJ
 * @date 2014-3-3
 * @package j.android.library.base -- BaseService.java
 * @Description service基类
 */
public abstract class BaseService extends Service {

	public class ServiceBinder extends Binder{
		public BaseService getServiceInstance(){
			return BaseService.this;
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		onBindService(intent);
		return new ServiceBinder();
	}

	public void onBindService(Intent intent){}

	protected PendingIntent getCanBackApplicationPendingIntent(){return getCanBackApplicationPendingIntent(NotificationReturnActivity.class);}
	protected PendingIntent getCanBackApplicationPendingIntent(Class<? extends Activity> activityClz){return getCanBackApplicationPendingIntent(activityClz, null);}
	protected PendingIntent getCanBackApplicationPendingIntent(Intent intent){return getCanBackApplicationPendingIntent(null, intent);}
    protected PendingIntent getCanBackApplicationPendingIntent(Class<? extends Activity> activityClz,Intent intent){
    	if (null==intent) {
    		intent=new Intent(this, activityClz);
		}
    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}


