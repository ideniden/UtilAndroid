package com.luoj.android.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.hardware.ConsumerIrManager.CarrierFrequencyRange;

/**
 * @author LuoJ
 * @date 2015-7-6
 * @package j.android.library.utils -- IRUtil.java
 * @Description 
 */
@TargetApi(19)
public class IRUtil {

	/**
	 * 判断设备是否存在红外发射装置
	 * @param ctx
	 * @return
	 */
	public static boolean hasIrEmitter(Context ctx){
		try {
			ConsumerIrManager cirm=(ConsumerIrManager) ctx.getSystemService(Context.CONSUMER_IR_SERVICE);
			return cirm.hasIrEmitter();
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 获取载频范围
	 * @param ctx
	 * @return
	 */
	public static CarrierFrequencyRange[] getCarrierFrequencies(Context ctx){
		ConsumerIrManager cirm=(ConsumerIrManager) ctx.getSystemService(Context.CONSUMER_IR_SERVICE);
		return cirm.getCarrierFrequencies();
	}
	
	/**
	 * 发射信号
	 * @param ctx
	 * @param carrierFrequency
	 * @param pattern
	 */
	public static void transmit(Context ctx,int carrierFrequency, int[] pattern){
		ConsumerIrManager cirm=(ConsumerIrManager) ctx.getSystemService(Context.CONSUMER_IR_SERVICE);
		cirm.transmit(carrierFrequency, pattern);
	}
	
}


