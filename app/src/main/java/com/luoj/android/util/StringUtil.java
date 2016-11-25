package com.luoj.android.util;

import android.text.TextUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author LuoJ
 * @date 2014-10-22
 * @package j.android.library.utils -- StringUtil.java
 * @Description
 */
public class StringUtil {

//	/**
//	 *
//	 * @param chstr
//	 * @return
//	 */
//	public static String getFirstLetter(String chstr) {
//		if (!TextUtils.isEmpty(chstr)) {
//			return String.valueOf(toPinyin(chstr, true).charAt(0));
//		}
//		return "";
//	}
//
//	public static String getFirstLetter(String chstr, String defaultLetter) {
//		if (!TextUtils.isEmpty(chstr)) {
//			Pattern p = Pattern.compile("[A-Z]");
//			String firstLetter = getFirstLetter(chstr).toUpperCase();
//			return p.matcher(firstLetter).find() ? firstLetter : defaultLetter;
//		}
//		return "";
//	}

	// 将中文串转换在拼音串, 可选择是否只需要首字母(first letter)
//	public static String toPinyin(String chstr, boolean firstFlag) {
//		StringBuffer str = new StringBuffer("");
//		if (chstr != null && chstr.length() > 0) {
//			try {
//				HanyuPinyinOutputFormat G_PINYIN_FORMAT = null;
//				if (G_PINYIN_FORMAT == null) {
//					G_PINYIN_FORMAT = new HanyuPinyinOutputFormat();
//					G_PINYIN_FORMAT.setCaseType(HanyuPinyinCaseType.LOWERCASE);
//					G_PINYIN_FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//				}
//
//				char[] carray = chstr.toCharArray();
//				for (int i = 0; i < carray.length; i++) {
//					if (carray[i] >= 0x4e00 && carray[i] <= 0x9fbb) {// 判断是否为中文
//						String[] tmp = PinyinHelper.toHanyuPinyinStringArray(carray[i], G_PINYIN_FORMAT);
//						if (tmp != null && tmp.length > 0 && tmp[0] != null) {
//							if (firstFlag) {
//								str.append(tmp[0].charAt(0));
//							} else {
//								for (int n = 0; n < tmp[0].length(); n++) {
//									str.append(tmp[0].charAt(n));
//								}
//							}
//						}
//					} else {
//						str.append(carray[i]);
//					}
//				}
//				return str.toString();
//			} catch (Exception err) {
//				LogUtil.e("pinyin() error, " + chstr + ", " + err.getMessage());
//			}
//		}
//
//		return chstr;
//	}

	public static boolean isMatchAccountOrPassword(String s) {
		Pattern pattern = Pattern.compile("[a-zA-Z][a-zA-z0-9]{2,12}");
		Matcher matcher = pattern.matcher(s);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isName(String str) {
		if (str.matches("^[\u4e00-\u9fa5-a-zA-Z0-9]+")) {
			return true;
		}
		return false;
	}

	public static boolean isAge(String str) {
		if (str.matches("[0-9]+")) {
			return true;
		}
		return false;
	}

	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);

		return m.matches();
	}

	public static String getNotNullStr(CharSequence s) {
		if (null == s)
			return "";
		String str = s.toString();
		if (TextUtils.isEmpty(str) || str.replace(" ", "").length() == 0 || "null".equals(str.trim())) {
			return "";
		}
		return str;
	}

	public static String code2code(String strIn, String sourceCode, String targetCode) {
		String strOut = null;
		if (strIn == null || (strIn.trim()).equals(""))
			return strIn;
		try {
			byte[] b = strIn.getBytes(sourceCode);
			strOut = new String(b, targetCode);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return strOut;
	}

	public static int parseToInt(String target) {
		return parseToInt(target, -1);
	}

	public static int parseToInt(String target, int defaultValue) {
		int result = 0;
		try {
			result = Integer.parseInt(target);
			return result;
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	@SuppressWarnings("rawtypes")
	public String toString(Object object) {
		Class clazz = object.getClass();// 获得代表该类的Class对象
		StringBuilder sb = new StringBuilder();// 利用StringBuilder来保存字符串stringBuilder很不错的，完全可以替代string,最主要的是它的效率高
		Package packageName = clazz.getPackage();// 获得类所在的包
		sb.append("包名：" + packageName.getName() + "\t");// 输出类所在的包
		String className = clazz.getSimpleName();// 获得类的简单名称
		sb.append("类名：" + className + "\n");// 输出类的简单名称
		sb.append("公共构造方法：\n");
		// 获得所有代表构造方法的Constructor数组
		Constructor[] constructors = clazz.getDeclaredConstructors();
		for (Constructor constructor : constructors) {
			String modifier = Modifier.toString(constructor.getModifiers());// 获得修饰符
			if (modifier.contains("public")) {// 查看修饰符是否含有“public”
				sb.append(constructor.toGenericString() + "\n");
			}
		}
		sb.append("公共域：\n");
		Field[] fields = clazz.getDeclaredFields();// 获得代表所有域的Field数组
		for (Field field : fields) {
			String modifier = Modifier.toString(field.getModifiers());
			if (modifier.contains("public")) {// 查看修饰符是否含有“public”
				sb.append(field.toGenericString() + "\n");
			}
		}
		sb.append("公共方法：\n");
		Method[] methods = clazz.getDeclaredMethods();// 获得代表所有方法的Method[]数组
		for (Method method : methods) {
			String modifier = Modifier.toString(method.getModifiers());
			if (modifier.contains("public")) {// 查看修饰符是否含有“public”
				sb.append(method.toGenericString() + "\n");
			}
		}
		return sb.toString();

	}

	public static boolean isEmpty(CharSequence str) {
		if (str == null || str.length() == 0)
			return true;
		else
			return false;
	}

	public static boolean isNull(String str){
		boolean ret=isEmpty(str);
		if(!ret){
			return "null".equals(str.toLowerCase());
		}
		return false;
	}

	public static boolean isNumeric(String str){
		for (int i = 0; i < str.length(); i++){
			if (!Character.isDigit(str.charAt(i))){
				return false;
			}
		}
		return true;
	}

}
