package com.luoj.android.util.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.luoj.android.util.LogUtil;
import com.luoj.android.util.ToastUtil;
import com.luoj.android.util.thread.template.AbsLoopThread;

import java.util.ArrayList;
import java.util.List;


/**
 * @author LuoJ
 * @date 2014-1-23
 * @package com.luoj.bluetooth.utils -- BluetoothConnectUtil.java
 * @Description 
 */
public class BluetoothUtil {

	public static final int REQUEST_OPEN_BLUETOOTH=1;
	
	public static final int SEARCH_OVER=0x123456;
	
	private Context mctx;
	
	private Activity activity;
	
	private BluetoothUtilCallBack mBluetoothUtilCallBack;
	
	private Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			if(null!=mBluetoothUtilCallBack)mBluetoothUtilCallBack.handleMsg(msg);
		};
	};
	
	private static BluetoothUtil mBluetoothConnectUtil;
	
	public static BluetoothUtil getInstance(Context mctx,BluetoothUtilCallBack bluetoothUtilCallBack){
		if (null==mBluetoothConnectUtil) {
			mBluetoothConnectUtil=new BluetoothUtil(mctx,bluetoothUtilCallBack);
		}
		return mBluetoothConnectUtil;
	}

	public BluetoothUtil(Context mctx,BluetoothUtilCallBack bluetoothUtilCallBack) {
		this.mctx = mctx;
		this.activity=(Activity)mctx;
		this.mBluetoothUtilCallBack=bluetoothUtilCallBack;
		mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
		if (null==mBluetoothAdapter) {
			ToastUtil.showToast(mctx, "此设备无蓝牙功能");
			return;
		}
	}
	
	private BluetoothAdapter mBluetoothAdapter;
	
	private DiscoveryDevicesReceiver mDiscoveryDevicesReceiver;
	
	private List<BluetoothDevice> mBluetoothDevices=new ArrayList<BluetoothDevice>();
	
	private SearchListener mSearchListener;
	
	//---------------------------------------------------------------------------------------------------------
	
	/**
	 * 打开蓝牙
	 */
	public void openBluetooth(){
		if (!mBluetoothAdapter.isEnabled()) {
			activity.startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_OPEN_BLUETOOTH);
		}else{
			ToastUtil.showToast(mctx, "蓝牙已开启");
		}
	}
	
	public boolean isOpen(){
		return mBluetoothAdapter.isEnabled();
	}
	
	/**
	 * 设置设备可见
	 * @param durationTime
	 */
	public void setDeviceDiscoverability(int durationTime){
		Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, durationTime);
		activity.startActivity(discoverableIntent);
	}
	
	/**
	 * 搜索蓝牙设备（如果正在搜索，则停止）
	 * @param mTextView
	 */
	public void searchDevices(TextView mTextView){
		if(!mBluetoothAdapter.isDiscovering()){
			mTextView.setText("蓝牙设备搜索中...");
			mBluetoothDevices.clear();
			registerMyReceiver();
			mBluetoothAdapter.startDiscovery();
			
			mSearchListener=new SearchListener();
			mSearchListener.setSleepTime(200);
			mSearchListener.startThread();
		}else{
			mTextView.setText("开始搜索");
			unRegisterReceriver();
			mBluetoothAdapter.cancelDiscovery();
			if(null!=mSearchListener)mSearchListener.stopThread();
			handler.sendEmptyMessage(SEARCH_OVER);
		}
	}
	
	/**
	 * 接收搜索到的设备
	 */
	public void registerMyReceiver(){
		if (null==mDiscoveryDevicesReceiver) {
			mDiscoveryDevicesReceiver=new DiscoveryDevicesReceiver(handler,mBluetoothDevices);
			IntentFilter filter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
			mctx.registerReceiver(mDiscoveryDevicesReceiver, filter);
		}
	}
	
	/**
	 * 解除广播
	 */
	public void unRegisterReceriver(){
		if (null!=mDiscoveryDevicesReceiver) {
			mctx.unregisterReceiver(mDiscoveryDevicesReceiver);
			mDiscoveryDevicesReceiver=null;
		}
	}
	
	public List<BluetoothDevice> getBluetoothDevices(){
		return mBluetoothDevices;
	}
	
	//循环线程检测查询状态，如果停止表明查询结束
	class SearchListener extends AbsLoopThread<Boolean> {
		@Override
		protected Boolean longTimeOperate() {
			boolean discovering = mBluetoothAdapter.isDiscovering();
			LogUtil.d("discovering-->"+discovering);
			return discovering;
		}
		@Override
		protected void handleResult(Boolean result) {
			if (!result) {
				BluetoothUtil.this.handler.sendEmptyMessage(SEARCH_OVER);
				stopThread();
			}
		}
	}
	
	public interface BluetoothUtilCallBack{
		public void handleMsg(Message msg);
	}
	
}


