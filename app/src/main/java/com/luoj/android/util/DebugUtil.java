package com.luoj.android.util;

/**
 * @author LuoJ
 * @date 2014-5-9
 * @package j.android.library.utils -- DebugUtil.java
 * @Description Debug工具类
 */
public class DebugUtil {

	public static MethodExecTimeTrace startMethodExecTimeTrace(){
		return new MethodExecTimeTrace();
	}
	
	public static class MethodExecTimeTrace{
		long startPoint;

		public MethodExecTimeTrace() {
			startPoint=System.currentTimeMillis();
		}
		public long startTrace(){
			startPoint=System.currentTimeMillis();
			return startPoint;
		}
		public long stopTrace(){
			return stopTrace("");
		}
		public long stopTrace(String flag){
			long stopPoint=System.currentTimeMillis();
			long execTime=stopPoint-startPoint;
			LogUtil.d(flag+"执行时间：["+execTime+"ms]、["+(execTime/1000)+"s]");
			return execTime;
		}
	}
	
}


