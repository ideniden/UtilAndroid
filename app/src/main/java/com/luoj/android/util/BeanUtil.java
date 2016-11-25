package com.luoj.android.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * @author LuoJ
 * @date 2013-10-25
 * @package j.android.library.utils.reflect -- BeanUtil.java
 * @Description bean辅助类。提供反射调用bean的get、set方法
 */
public class BeanUtil {
	
	/**
	 * java反射bean的get方法.
	 * @param objectClass
	 * @param fieldName
	 * @return
	 */
	public static Method getGetMethod(Class<?> objectClass, String fieldName) {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("get");
			sb.append(fieldName.substring(0, 1).toUpperCase());
			sb.append(fieldName.substring(1));
			return objectClass.getMethod(sb.toString());
		}catch(NoSuchMethodException e){
			try {
				StringBuffer sb = new StringBuffer();
				sb.append("get");
				sb.append(fieldName.substring(0, 1));
				sb.append(fieldName.substring(1));
				return objectClass.getMethod(sb.toString());
			} catch (Exception e2) {
			}
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 
	 * 执行get方法.
	 * @param o 执行对象
	 * @param fieldName 属性
	 */
	public static Object invokeGet(Object o, String fieldName) {
		Method method = getGetMethod(o.getClass(), fieldName);
		try {
			return method.invoke(o, new Object[0]);
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * java反射bean的set方法.
	 * @param objectClass
	 * @param fieldName
	 * @return
	 */
	public static Method getSetMethod(Class<?> objectClass, String fieldName) {
		try {
			Class<?>[] parameterTypes = new Class[1];
			Field field = objectClass.getDeclaredField(fieldName);
			parameterTypes[0] = field.getType();
			StringBuffer sb = new StringBuffer();
			sb.append("set");
			sb.append(fieldName.substring(0, 1).toUpperCase());
			sb.append(fieldName.substring(1));
			return objectClass.getMethod(sb.toString(), parameterTypes);
		}catch(NoSuchMethodException e){
			try {
				Class<?>[] parameterTypes = new Class[1];
				Field field = objectClass.getDeclaredField(fieldName);
				parameterTypes[0] = field.getType();
				StringBuffer sb = new StringBuffer();
				sb.append("set");
				sb.append(fieldName.substring(0, 1));
				sb.append(fieldName.substring(1));
				return objectClass.getMethod(sb.toString(), parameterTypes);
			} catch (Exception e2) {
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * 执行set方法.
	 * @param o 执行对象
	 * @param fieldName 属性
	 * @param value 值
	 */
	public static Object invokeSet(Object o, String fieldName, Object value) {
		Method method = getSetMethod(o.getClass(), fieldName);
		Class<?> fieldType = method.getParameterTypes()[0];
		Object typeCast = value;
		DataType type = DataType.getType(fieldType);
		if (null!=type) {
			typeCast=type.typeCast(value);
		}
		try {
			return method.invoke(o, typeCast);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 去除className中包含的包名.
	 * @param fullClassName
	 * @return
	 */
	public static String removePackName(String fullClassName){
		String result="";
		if (null!=fullClassName&&!"".equals(fullClassName)) {
			result=fullClassName.substring(fullClassName.lastIndexOf(".")+1, fullClassName.length());
		}
		return result;
	}
	
}
