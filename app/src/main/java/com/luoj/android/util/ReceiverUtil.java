package com.luoj.android.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.luoj.android.util.broadcast.BaseBroadcastReceiver;

import java.util.ArrayList;


/**
 * Created by LuoJ on 2014/11/13.
 */
public class ReceiverUtil {

    private static ArrayList<BroadcastReceiver> receivers=new ArrayList<BroadcastReceiver>();

    public static void registReceiver(Context context,String action,BroadcastReceiver receiver){
        if (null==receiver){
            LogUtil.d("注册广播失败，请检查receiver参数");
            return;
        }
        context.registerReceiver(receiver,new IntentFilter(action));
        receivers.add(receiver);
    }
    
    public static void registReceiver(Context context,BaseBroadcastReceiver receiver){
    	if(TextUtils.isEmpty(receiver.getAction())){
    		LogUtil.e("ReceiverUtil.registReceiver(BaseBroadcastReceiver) error:继承自BaseBroadcastReceiver之后，必须实现setActionString函数且返回值不能为空");
    		return;
    	}
    	registReceiver(context, receiver.getAction(), receiver);
    }
    
    public static void unRegistReceiver(Context context,BroadcastReceiver receiver){
        if (null==receiver){
            LogUtil.d("解除广播失败，请检查receiver参数");
            return;
        }
        if (!receivers.contains(receiver)){
            LogUtil.d("解除广播失败，目标广播已被解除或没有通过ReceiverUtil.registReceiver函数来注册");
            return;
        }
        try {
        	context.unregisterReceiver(receiver);
		} catch (Exception e) {
			LogUtil.d("解除广播失败，目标广播已被解除或没有通过ReceiverUtil.registReceiver函数来注册\n->"+e.toString());
		}
		receivers.remove(receiver);
    }


//    public static void unRegistAllReceivers(){
//        if (null==receivers||receivers.isEmpty())return;
//        for (BroadcastReceiver r:receivers){
//            try {
//				BaseApplication.getInstance().unregisterReceiver(r);
//			} catch (Exception e) {
//				LogUtil.e("注意！发现有receiver不是通过本工具类解绑 Class->"+r.getClass());
//			}
//        }
//        receivers.clear();
//    }

}
