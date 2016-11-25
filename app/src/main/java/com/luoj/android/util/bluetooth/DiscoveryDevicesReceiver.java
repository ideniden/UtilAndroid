package com.luoj.android.util.bluetooth;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.luoj.android.util.LogUtil;

import java.util.List;

/**
 * @author LuoJ
 * @date 2014-1-23
 * @package com.luoj.bluetooth.receiver -- DiscoveryDevicesReceiver.java
 * @Description 
 */
public class DiscoveryDevicesReceiver extends BroadcastReceiver {

	private List<BluetoothDevice> mBluetoothDevices;
	
	private Handler handler;
	
	public DiscoveryDevicesReceiver(Handler handler,List<BluetoothDevice> mBluetoothDevices) {
		this.handler=handler;
		this.mBluetoothDevices = mBluetoothDevices;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		LogUtil.d("searchDevices action->"+intent.getAction());
		if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(intent.getAction())) {
			handler.sendEmptyMessage(0);
		}
		if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
			BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			mBluetoothDevices.add(device);
			handler.sendEmptyMessage(1);
		}
		if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())) {
			handler.sendEmptyMessage(2);
		}
	}

}


