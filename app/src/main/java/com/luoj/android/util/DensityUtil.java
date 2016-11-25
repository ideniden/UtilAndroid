package com.luoj.android.util;

import android.content.Context;

/**
 * @author LuoJ
 * @date 2014-9-4
 * @package j.android.library.utils -- DensityUtil.java
 * @Description 像素以及密度转换工具类
 */
public class DensityUtil {
	
	/** 
     * 根据手机的分辨率dp 转成(像素)
     */  
    public static int dip2px(float density, float dpValue) {  
        return (int) (dpValue * density + 0.5f);  
    }  
	
    /** 
     * 根据手机的分辨率dp 转成(像素)
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     *根据手机的分辨率px(像素) 的单转成dp 
     */  
    public static int px2dip(float density, float pxValue) {  
        return (int) (pxValue / density + 0.5f);  
    } 
    
    /** 
     *根据手机的分辨率px(像素) 的单转成dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
}

