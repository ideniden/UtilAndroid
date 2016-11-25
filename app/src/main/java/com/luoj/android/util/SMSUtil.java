package com.luoj.android.util;

import java.util.List;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

/**
 * @author LuoJ
 * @date 2015-7-27
 * @package j.android.library.utils -- SMSUtil.java
 * @Description 
 */
public class SMSUtil {

	/**
	 * 发送短信
	 * @param ctx
	 * @param phoneNumber
	 * @param message
	 */
	public static void sendSMS(Context ctx,String phoneNumber, String message) {
        Intent sentIntent = new Intent("SENT_SMS_ACTION");  
        PendingIntent sentPI = PendingIntent.getBroadcast(ctx,0,sentIntent,0);  
        Intent deliverIntent = new Intent("DELIVERED_SMS_ACTION");  
        PendingIntent deliverPI = PendingIntent.getBroadcast(ctx,0,deliverIntent,0);  
        SmsManager sms = SmsManager.getDefault();  
        if (message.length() > 70) {  
            List<String> msgs = sms.divideMessage(message);  
            for (String msg : msgs) {  
                sms.sendTextMessage(phoneNumber, null, msg, sentPI, deliverPI);  
            }  
        } else {  
            sms.sendTextMessage(phoneNumber, null, message, sentPI, deliverPI);  
        }
    }
	
	public static void sendSMS(Context ctx,SMSInfo smsInfo) {
		sendSMS(ctx, smsInfo.phoneNumber, smsInfo.message);
	}
	
	public static void sendSMS(Context ctx,List<SMSInfo> smsInfos) {
		for (SMSInfo smsInfo : smsInfos) {
			sendSMS(ctx, smsInfo);
		}
	}
	
	public static class SMSInfo{
		public String phoneNumber;
		public String message;
		public SMSInfo(String phoneNumber, String message) {
			this.phoneNumber = phoneNumber;
			this.message = message;
		}
	}
	
}
