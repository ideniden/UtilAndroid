package com.luoj.android.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author LuoJ
 * @date 2014-9-4
 * @package j.android.library.utils -- DateUtil.java
 * @Description 日期处理工具类
 */
public class DateUtil {
	
	public static final String YEAR_MONTH_DAY="yyyy-MM-dd";
	public static final String YEAR_MONTH_DAY_HOUR_MINUTE_SECOND="yyyy-MM-dd HH:mm:ss";
	public static final String YEAR_MONTH_DAY_HOUR_MINUTE="yyyy-MM-dd HH:mm";
	public static final String HOUR_MINUTE_SECOND="hh:mm:ss";
	public static final String HOUR_MINUTE="hh:mm";
	public static final String HOUR_MINUTE_24="HH:mm";

    public static String currentDateTime(){
        return transDate(System.currentTimeMillis(),YEAR_MONTH_DAY_HOUR_MINUTE_SECOND);
    }

    public static String currentDateTime(String pattern){
        return transDate(System.currentTimeMillis(),pattern);
    }

	public static String transDate(long ms,String pattern){
		Date date = new Date(ms);
		SimpleDateFormat format = new SimpleDateFormat(pattern); 
		return format.format(date); 
	}
	
	public static String transDate(String ms,String pattern){
		Date date = new Date(Long.valueOf(ms));
		SimpleDateFormat format = new SimpleDateFormat(pattern); 
		return format.format(date); 
	}
	
	public static String ms2Timestamp(String ms){
		Date date = new Date(Long.valueOf(ms));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		return format.format(date); 
	}

    public static boolean isInValidTime(String time,long validDurationTime){
        if(TextUtils.isEmpty(time)){
            LogUtil.e("time参数为空,无法做判断");
            return false;
        }
        try {
            long t=Long.valueOf(time);
            return isInValidTime(t,validDurationTime);
        } catch (NumberFormatException e) {
            LogUtil.e("time参数非时间戳,无法做判断");
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isInValidTime(long time,long validDurationTime){
        long curTime=System.currentTimeMillis();
        long c=(curTime-time);
        return (c<validDurationTime)?true:false;
    }

    //秒转HH:mm:ss
    public static String secToTime(int time) {  
        String timeStr = null;  
        int hour = 0;  
        int minute = 0;  
        int second = 0;  
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }
    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
    
    public static String getTimeZoneWithGMT(){
    	return "GMT+" + (Calendar.getInstance().getTimeZone().getRawOffset()/3600000);
    }

    public static long getTimeMillis(String time,String pattern){
        SimpleDateFormat sdf=new SimpleDateFormat(pattern);
        try {
            return sdf.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
