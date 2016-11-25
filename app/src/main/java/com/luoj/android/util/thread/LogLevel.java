package com.luoj.android.util.thread;

/**
 * @author LuoJ
 * @date 2014-1-2
 * @package j.android.library.base -- LogLevel.java
 * @Description log打印等级
 */
public enum LogLevel {
	
	V("verbose"),D("debug"),I("info"),W("warning"),E("error");
	
	LogLevel(String description){
		this.description=description;
	}
	
	private String description;

	public String getDescription() {
		return description;
	}
	
}
