package com.luoj.android.util;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author LuoJ
 * @date 2014-9-4
 * @package j.android.library.utils -- MD5.java
 * @Description MD5加密
 */
public class MD5 {
	
	// 全局数组
	public final static String[] strDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	public MD5() {
	}

	private static String byteToArrayString(byte bByte) {
		int iRet = bByte;
		// System.out.println("iRet="+iRet);
		if (iRet < 0) {
			iRet += 256;
		}
		int iD1 = iRet / 16;
		int iD2 = iRet % 16;
		return strDigits[iD1] + strDigits[iD2];
	}

	@SuppressWarnings("unused")
	private static String byteToNum(byte bByte) {
		int iRet = bByte;
		System.out.println("iRet1=" + iRet);
		if (iRet < 0) {
			iRet += 256;
		}
		return String.valueOf(iRet);
	}

	private static String byteToString(byte[] bByte) {
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < bByte.length; i++) {
			sBuffer.append(byteToArrayString(bByte[i]));
		}
		return sBuffer.toString();
	}

	public static String encode(String str){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return byteToString(md.digest(str.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String encodeAndToUpperCase(String str){
		String encodeStr = encode(str);
		return TextUtils.isEmpty(encodeStr)?null:encodeStr.toUpperCase();
	}

//	public static void main(String[] args) {
//		MD5 getMD5 = new MD5();
//		System.out.println(getMD5.encode("12345678"));
//	}

}
