package com.luoj.android.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import java.util.Random;

/**
 * @author LuoJ
 * @date 2015-1-13
 * @package j.android.library.utils -- NotificationUtil.java
 * @Description 
 */
public class NotificationUtil {

	public static void showMessageOnNotification(Context ctx,String title,String content){showMessageOnNotification(ctx,title, content,null);}

    public static void showMessageOnNotification(Context ctx,String title,String content,Intent intent){
        showMessageOnNotification(ctx,102,"您有一条新消息",title,content,intent,false);
    }

    public static void showMessageOnNotification(Context ctx,int notifyID,String tickerText,String title,String content,Intent intent){
        showMessageOnNotification(ctx,notifyID,tickerText,title,content,intent,false);
    }

	@SuppressWarnings("deprecation")
	public static void showMessageOnNotification(Context ctx,int notifyID,String tickerText,String title,String content,Intent intent,boolean ring){
		PendingIntent pi=null;
		if(null!=intent){
			pi=IntentUtil.getCanBackApplicationPendingIntent(ctx,intent);
		}
		NotificationManager mNotificationManager = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
//		Notification notification = new Notification(R.drawable.ic_launcher,"",System.currentTimeMillis());
//        notification.defaults = Notification.DEFAULT_SOUND;
//        notification.flags=Notification.FLAG_AUTO_CANCEL;
//		notification.tickerText=tickerText;
//		notification.setLatestEventInfo(ctx, title,content, pi);
        Notification.Builder builder=new Notification.Builder(ctx);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(),R.mipmap.ic_launcher));
        builder.setAutoCancel(true);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setContentIntent(pi);
        builder.setTicker(tickerText);
		mNotificationManager.notify(notifyID, builder.build());
	}

    /**
     * 消息响铃
     * @param context
     * @return
     */
    public static int messageRing(Context context) {
        NotificationManager mgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification nt = new Notification();
        nt.defaults = Notification.DEFAULT_SOUND;
        int soundId = new Random(System.currentTimeMillis()).nextInt(Integer.MAX_VALUE);
        mgr.notify(soundId, nt);
        return soundId;
    }

    public static void cancel(Context ctx,int notifyId){
        NotificationManager nm=(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        if(-1!=notifyId)nm.cancel(notifyId);
    }

}


