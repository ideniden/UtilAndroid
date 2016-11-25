package com.luoj.android.util;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.TrafficStats;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * @author LuoJ
 * @date 2014-3-18
 * @package j.android.library.utils -- NetUtil.java
 * @Description 网络工具类
 */
public class NetStatusUtil {

	/**
	 * 判断有无网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNetwork(final Activity context) {
		boolean netSataus = false;
		ConnectivityManager cwjManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		cwjManager.getActiveNetworkInfo();
		if (cwjManager.getActiveNetworkInfo() != null) {
			netSataus = cwjManager.getActiveNetworkInfo().isAvailable();
		} else {
			// 提示对话框
			Builder builder = new Builder(context);
			builder.setTitle("网络设置提示").setMessage("网络连接不可用,是否进行设置?").setPositiveButton("2G/3G设置", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
					context.startActivityForResult(intent, 0);
				}
			}).setNeutralButton("wifi设置", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
					context.startActivityForResult(intent, 0);
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).show();
		}
		return netSataus;
	}

	public static boolean NetState(Context context) {
		return NetState(context, false);
	}

	/**
	 * 检查网络状态
	 * 
	 * @param context
	 * @return
	 */
	public static boolean NetState(Context context, boolean isShowToast) {
		boolean flag = false;
		if (null == context) {
			return flag;
		}
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 获取代表联网状态的NetWorkInfo对象
		NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
		// 获取当前的网络连接是否可用
		if (null == networkInfo) {
			if (isShowToast)
				Toast.makeText(context, "当前的网络连接不可用", Toast.LENGTH_SHORT).show();
			flag = false;
		} else {
			boolean available = networkInfo.isAvailable();
			if (available) {
				flag = true;
			} else {
				Log.i("通知", "当前的网络连接不可用");
				if (isShowToast)
					Toast.makeText(context, "当前的网络连接不可用", Toast.LENGTH_SHORT).show();
			}
		}

		NetworkInfo mobileNetWorkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		State state = null;
		if (null != mobileNetWorkInfo) {
			state = mobileNetWorkInfo.getState();
		}
		if (State.CONNECTED == state) {
			Log.d("NetStatusUtil", "GPRS网络已连接");
		}

		NetworkInfo wifiNetWorkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		state = null;
		if (null != wifiNetWorkInfo) {
			state = wifiNetWorkInfo.getState();
		}
		if (State.CONNECTED == state) {
			Log.d("NetStatusUtil", "WIFI网络已连接");
		}
		return flag;

		// // 跳转到无线网络设置界面
		// startActivity(new
		// Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
		// // 跳转到无限wifi网络设置界面
		// startActivity(new
		// Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));

	}

	/**
	 * 获取网络类型 两种：手机、WIFI
	 * 
	 * @return
	 */
	public static String getNetworkType(Context context) {
		ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectMgr.getActiveNetworkInfo();
		if (null == info) {
			return null;
		}
		switch (info.getType()) {
		case ConnectivityManager.TYPE_WIFI:
			return "wifi";
		case ConnectivityManager.TYPE_MOBILE:
			switch (info.getSubtype()) {
			case TelephonyManager.NETWORK_TYPE_CDMA:// 网络类型为CDMA,电信的2G
				return "CDMA";
			case TelephonyManager.NETWORK_TYPE_EDGE:// 网络类型为EDGE,移动和联通的2G
				return "EDGE";
			case TelephonyManager.NETWORK_TYPE_EVDO_0:// 网络类型为EVDO0,电信的3G
			case TelephonyManager.NETWORK_TYPE_EVDO_A:// 网络类型为EVDOA,电信的3G
				return "EVDO";
			case TelephonyManager.NETWORK_TYPE_GPRS:// 网络类型为GPRS,移动和联通的2G
				return "GPRS";
			case TelephonyManager.NETWORK_TYPE_HSDPA:// 网络类型为HSDPA,联通的3G
				return "HSDPA";
			case TelephonyManager.NETWORK_TYPE_HSPA:// 网络类型为HSPA
				return "HSPA";
			case TelephonyManager.NETWORK_TYPE_HSUPA:// 网络类型为HSUPA
				return "HSUPA";
			case TelephonyManager.NETWORK_TYPE_UMTS:// 网络类型为UMTS,联通的3G
				return "UMTS";
			}
			return "mobile_unknown";
		}
		return null;
	}

	public static final int WIFI = 1;
	public static final int MOBILE = 0;
	public static final int CMNET = 10;
	public static final int CMWAP = 20;

	public static int getAPNType(Context context) {
		int netType = -1;
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			LogUtil.e("networkInfo.getExtraInfo() is " + networkInfo.getExtraInfo());
			if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
				netType = CMNET;
			} else {
				netType = CMWAP;
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = WIFI;
		}
		return netType;
	}

	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info = mgr.getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	private static long lastTotalRxBytes = 0;
    private static long lastTimeStamp = 0;
	
    public static long getCurrentSpeedKB(Context ctx){
    	long nowTotalRxBytes = TrafficStats.getUidRxBytes(ctx.getApplicationInfo().uid)==TrafficStats.UNSUPPORTED ? 0 :(TrafficStats.getTotalRxBytes()/1024);
    	long nowTimeStamp = System.currentTimeMillis();
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));//毫秒转换
        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;
        return speed;
    }
    
}
