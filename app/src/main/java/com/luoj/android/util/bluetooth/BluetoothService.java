package com.luoj.android.util.bluetooth;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import com.luoj.android.util.LogUtil;


/**
 * @author LuoJ
 * @date 2014-2-18
 * @package com.luoj.bluetooth.service -- BluetoothOperater.java
 * @Description 
 */
public class BluetoothService extends Service {
	
	private BluetoothConnUtil mBluetoothService;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogUtil.d("蓝牙操作服务开启...");
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return new MyBinder();
	}

	public class MyBinder extends Binder{
		public BluetoothService getServiceInstance(Handler handler){
			if (null==mBluetoothService) {
				mBluetoothService=new BluetoothConnUtil(BluetoothService.this);
			}
			mBluetoothService.setHandler(handler);
			return BluetoothService.this;
		}
	} 
	
	public BluetoothConnUtil getBluetoothService(){
		return mBluetoothService;
	}
	
}


