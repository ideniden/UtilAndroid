package com.luoj.android.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.luoj.android.util.service.BaseService;

import java.util.HashMap;


/**
 * @author LuoJ
 * @date 2014-10-20
 * @package j.android.library.utils -- ConnectionUtil.java
 * @Description 
 */
public class ConnectionUtil {

	private static HashMap<String, ServiceConnection> serviceConnectionPoll=new HashMap<String, ServiceConnection>();
	public static <T extends BaseService> void connectService(Context context, final ServiceConnectionInterface<T> callback){
		Class<?> service = ClassUtil.getClassGenericType(callback);
		if (null==service) {
			throw new RuntimeException("回调的匿名内部类必须指定泛型,并且必须继承自BaseService");
		}else{
			try {
				Object serObj = service.newInstance();
				if(!(serObj instanceof BaseService)){
					throw new RuntimeException("回调的匿名内部类必须指定泛型的类型为BaseService的子类");
				}
				serObj=null;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		String key=getConnectionKey(context, service);
		ServiceConnection c = serviceConnectionPoll.get(key);
		if (null!=c) {
			LogUtil.e(service.getCanonicalName()+"服务已经绑定在"+context.getClass().getCanonicalName());
			return;
		}
		ServiceConnection conn=new ServiceConnection() {
			@Override
			public void onServiceDisconnected(ComponentName name) {
				callback.connectDisconnected(name);
			}
			@SuppressWarnings("unchecked")
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				try {
					BaseService.ServiceBinder sb = (BaseService.ServiceBinder) service;
					callback.connectSuccess((T)sb.getServiceInstance());
				}catch(ClassCastException e){
					throw new RuntimeException("通过ConnectionUtil绑定的服务必须继承自BaseService");
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		context.bindService(new Intent(context, service), conn, Context.BIND_AUTO_CREATE);
		serviceConnectionPoll.put(key, conn);
	}
	
	public static <T extends BaseService> void disconnectService(Context context,Class<T> service){
		String key=getConnectionKey(context, service);
		ServiceConnection conn = serviceConnectionPoll.get(key);
		if (null!=conn) {
			context.unbindService(conn);
			serviceConnectionPoll.remove(key);
			conn=null;
		}
	}
	
	private static HashMap<String, Class<? extends BaseService>> serviceStartedPoll=new HashMap<String, Class<? extends BaseService>>();
	public static <T extends BaseService> void startService(Context context,Class<T> service){
		context.startService(new Intent(context, service));
		serviceStartedPoll.put(getConnectionKey(context, service), service);
	}
	public static <T extends BaseService> void stopService(Context context,Class<T> service){
		context.stopService(new Intent(context, service));
		serviceStartedPoll.remove(getConnectionKey(context, service));
	}
	public static void stopAllService(Context context){
		if(serviceStartedPoll.isEmpty())return;
		for (Class<? extends BaseService> service : serviceStartedPoll.values()) {
			context.stopService(new Intent(context, service));
		}
		serviceStartedPoll.clear();
	}
	
	private static String getConnectionKey(Context context,Class<?> service){
		return context.getClass().getCanonicalName()+"-"+service.getCanonicalName();
	}
	
	public interface ServiceConnectionInterface<T>{
		void connectDisconnected(ComponentName name);
		void connectSuccess(T service);
	}
	
}


