package com.luoj.android.util;

/**
 * @author LuoJ
 * @date 2014-9-4
 * @package j.android.library.utils -- TelPhoneUtil.java
 * @Description 拨号工具类
 */
public class TelPhoneUtil {
	
	public static TelPhoneUtil telPhoneUtil;
	
	private TelPhoneUtil() {
	}
	
	
	public static TelPhoneUtil newInstance(){
		if (null == telPhoneUtil) {
			telPhoneUtil = new TelPhoneUtil();
		}
		return telPhoneUtil;
	}
	
//	public void telPhone(Activity act,String telNum){
//		Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+telNum));
//		act.startActivity(intent);
//	}
	
}
