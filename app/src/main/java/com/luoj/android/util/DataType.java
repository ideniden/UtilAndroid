package com.luoj.android.util;

import java.lang.reflect.Field;

/**
 * @author LuoJ
 * @date 2014-3-11
 * @package j.android.library.base -- as.java
 * @Description 自定义数据类型(为枚举类型)、策略枚举
 */
public enum DataType{
	
	BYTE (byte.class){
		@Override
		public Object typeCast(Object value) {
			return Byte.parseByte((String) value);
		}
	},
	SHORT (short.class){
		@Override
		public Object typeCast(Object value) {
			short result = 0;
			if (null!=value&&!"".equals(value)) {
				result=Short.parseShort((String) value);
			}
			return result;
		}
	},
	INT (int.class){
		@Override
		public Object typeCast(Object value) {
			int result = 0;
			if (null!=value&&!"".equals(value)) {
				result=Integer.parseInt((String) value);
			}
			return result;
		}
	},
	LONG (long.class){
		@Override
		public Object typeCast(Object value) {
			long result = 0;
			if (null!=value&&!"".equals(value)) {
				result=Long.parseLong((String) value);
			}
			return result;
		}
	},
	FLOAT (float.class){
		@Override
		public Object typeCast(Object value) {
			float result = 0;
			if (null!=value&&!"".equals(value)) {
				result=Float.parseFloat((String) value);
			}
			return result;
		}
	},
	DOUBLE (double.class){
		@Override
		public Object typeCast(Object value) {
			double result = 0;
			if (null!=value&&!"".equals(value)) {
				result=Double.parseDouble((String) value);
			}
			return result;
		}
	},
	CHAR (char.class){
		@Override
		public Object typeCast(Object value) {
			String v=(String)value;
			if (null==v||"".equals(v)||v.length()>1) {
				throw new RuntimeException("值为空或者等于空字符串，又或者String类型的值大于一个[字符]长度，想要转char，请检查值");
			}
			return v.getBytes()[0];
		}
	},
	BOOLEAN (boolean.class){
		@Override
		public Object typeCast(Object value) {
			return Boolean.parseBoolean((String) value);
		}
	},
	BYTE_ARRAY (byte[].class){
		@Override
		public Object typeCast(Object value) {
			return ((String)value).getBytes();
		}
	},
	STRING(String.class){
		@Override
		public Object typeCast(Object value) {
			return value;
		}
		
	};
	
	DataType(Class<?> clz) {
		this.clz = clz;
	}

	Class<?> clz;
	
	public abstract Object typeCast(Object value);
	
	public static DataType getType(Object o, String fieldName){
		DataType type=null;
		try {
			Field field = o.getClass().getDeclaredField(fieldName);
			if (null!=field) {
				type=getType(field.getType());
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return type;
	}
	
	/**
	 * 根据数据类型的类类型，获取自定义枚举类型
	 * @param typeClazz
	 * @return
	 */
	public static DataType getType(Class<?> typeClazz){
		DataType[] values = values();
		for (int i = 0; i < values.length; i++) {
			if (values[i].clz.equals(typeClazz)) {
				return values[i];
			}
		}
		return null;
	}
	
}


