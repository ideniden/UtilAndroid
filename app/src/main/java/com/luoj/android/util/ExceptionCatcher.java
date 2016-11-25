package com.luoj.android.util;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author LuoJ
 * @date 2013-9-23
 * @package com.tosee.android.util -- ExceptionCatcher.java
 * @Description UncaughtException处理类，记录异常，并可做相关处理
 */
public class ExceptionCatcher implements UncaughtExceptionHandler {
	
	private Context mContext;
	
	private UncaughtExceptionHandler mDefaultHandler;// 系统默认的UncaughtException处理类
	
	private static ExceptionCatcher INSTANCE = new ExceptionCatcher();//处理类实例，饿汉式单例
	
//	private Map<String, String> infos = new HashMap<String, String>();// 用来存储设备信息和异常信息
	
	private GlobalExceptionInterfaces.IDoMore doMore;// 扩展接口
	
	private String pattern;//日期格式
	
	/**
	 * 私有构造函数
	 */
	private ExceptionCatcher() {}

	/** 获取CrashHandler实例 */
	public static ExceptionCatcher getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化
	 * @param mctx
	 */
	public void init(Context mctx) {
//		LogUtil.e("异常捕获开启");
//		LogUtil.e(mctx.getPackageName()+"___pid->" + Process.myPid());
		
		mContext = mctx;
		pattern="yyyy-MM-dd_HH-mm-ss";//默认日期格式
		
//		String imei=PhoneUtil.getPhoneIMEI(mContext);
//		if (null != imei)infos.put("IMEI", imei);//添加设备号信息
//		LogUtil.e("当前设备的IMEI-->" + infos.get("IMEI"));
//
//		String IMSI = PhoneUtil.getProvidersName(mContext);
//		if (null != IMSI)infos.put("PROVIDERS_NAME", IMSI);
		
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//杀掉出异常的应用
	        android.os.Process.killProcess(android.os.Process.myPid());
//			AppManager.getAppManager().AppExit(mContext);
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, PhoneUtil.isSystemInChinese()?"很抱歉,程序出现小状况,请重启软件。":"Sorry! Please restart the application", Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();
		// 保存错误信息(如果实现此接口则执行,实现接口的方法：handleExceptionData)
		if (null != doMore)doMore.onException(ex);
		return true;
	}

	/**
	 * 错误信息转成String
	 * @param ex
	 * @return
	 */
	public String parseThrowable2String(Throwable ex) {
		String result = null;
		if (null != ex) {
			Writer writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			ex.printStackTrace(printWriter);
			Throwable cause = ex.getCause();
			while (cause != null) {
				cause.printStackTrace(printWriter);
				cause = cause.getCause();
			}
			printWriter.close();
			result = writer.toString();
		}
		return result;
	}

//	/**
//	 * 收集设备参数信息
//	 * @param ctx
//	 */
//	private void collectDeviceInfo(Context ctx) {
//		AppUtil.getVersionInfo(mContext, infos);
//		PhoneUtil.getDeviceInfo(infos);
//	}

	/**
	 * 保存异常信息及设备信息到sd卡
	 * @param fileName
	 * @param savePath
	 */
	public void saveException2SDCard(final String fileName, final String savePath,final String content) {
		new Thread(){public void run() {
//				StringBuffer sb = new StringBuffer();
//				for (Map.Entry<String, String> entry : infos.entrySet()) {
//					if ("ERROR_CONTENT".equals(entry.getKey()))continue;
//					String key = entry.getKey();
//					String value = entry.getValue();
//					sb.append(key + "=" + value + "\n");
//				}
//			String result = infos.get("ERROR_CONTENT");
//			sb.append(result);
			try {
				if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
					File dir = new File(savePath);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					FileOutputStream fos = new FileOutputStream(savePath + fileName);
					fos.write(content.getBytes());
					fos.close();
				}
			} catch (Exception e) {
				LogUtil.e("an error occured while writing file...");
			}
		}}.start();
	}
	
//	/**
//	 * 保存异常信息到数据库中
//	 */
//	public void saveException2Database(){
//		saveException2Database(-1l, "");
//	}
//	public void saveException2Database(Long userId,String clientType){
//		try {
//			DaoFactory df=DaoFactory.getInstance();
//			GlobalExceptionDao dao = df.getSimpleDaoInstance(GlobalExceptionDao.class);
//			dao.init(mContext,false);
//			dao.insertData(getExpInfo(userId,clientType));
//		} catch (Exception e) {
//			LogUtil.e("初始化'异常数据库'操作类失败",e);
//		}
//	}
//
//	/**
//	 * 获取异常所有信息（包括设备信息）
//	 * @return
//	 */
//	public TbExceptionInfo getExpInfo() {
//		return getExpInfo(-1l, "");
//	}
//	public TbExceptionInfo getExpInfo(Long userId,String clientType) {
//		TbExceptionInfo exInfo = null;
//		if (!infos.isEmpty() && infos.size() > 0) {
//			exInfo = new TbExceptionInfo();
//			exInfo.setLoginId(userId);
//			exInfo.setLoginType(clientType);
//			exInfo.setImei(infos.get("IMEI"));
//			exInfo.setVersion(infos.get("versionCode"));
//			exInfo.setErrorTime(parseString2Date(infos.get("errorTime")));
//			for (Map.Entry<String, String> entry : infos.entrySet()) {
//				if ("BOARD".equals(entry.getKey())) {
//					exInfo.setBoard(entry.getValue());
//					continue;
//				}
//				if ("BRAND".equals(entry.getKey())) {
//					exInfo.setBrand(entry.getValue());
//					continue;
//				}
//				if ("CPU_ABI".equals(entry.getKey())) {
//					exInfo.setCpuAbi(entry.getValue());
//					continue;
//				}
//				if ("CPU_ABI2".equals(entry.getKey())) {
//					exInfo.setCpuAbi2(entry.getValue());
//					continue;
//				}
//				if ("DEVICE".equals(entry.getKey())) {
//					exInfo.setDevice(entry.getValue());
//					continue;
//				}
//				if ("DISPLAY".equals(entry.getKey())) {
//					exInfo.setDisplay(entry.getValue());
//					continue;
//				}
//				if ("FINGERPRINT".equals(entry.getKey())) {
//					exInfo.setFingerprint(entry.getValue());
//					continue;
//				}
//				if ("HARDWARE".equals(entry.getKey())) {
//					exInfo.setHardware(entry.getValue());
//					continue;
//				}
//				if ("HOST".equals(entry.getKey())) {
//					exInfo.setHost(entry.getValue());
//					continue;
//				}
//				if ("ID".equals(entry.getKey())) {
//					exInfo.setHostId(entry.getValue());
//					continue;
//				}
//				if ("IS_DEBUGGABLE".equals(entry.getKey())) {
//					exInfo.setIsDebuggable(entry.getValue());
//					continue;
//				}
//				if ("MANUFACTURER".equals(entry.getKey())) {
//					exInfo.setManufacturer(entry.getValue());
//					continue;
//				}
//				if ("MODEL".equals(entry.getKey())) {
//					exInfo.setModel(entry.getValue());
//					continue;
//				}
//				if ("PRODUCT".equals(entry.getKey())) {
//					exInfo.setProduct(entry.getValue());
//					continue;
//				}
//				if ("RADIO".equals(entry.getKey())) {
//					exInfo.setRadio(entry.getValue());
//					continue;
//				}
//				if ("SERIAL".equals(entry.getKey())) {
//					exInfo.setSerial(entry.getValue());
//					continue;
//				}
//				if ("TAGS".equals(entry.getKey())) {
//					exInfo.setTags(entry.getValue());
//					continue;
//				}
//				if ("TYPE".equals(entry.getKey())) {
//					exInfo.setType(entry.getValue());
//					continue;
//				}
//				if ("UNKNOWN".equals(entry.getKey())) {
//					exInfo.setUnkonwn(entry.getValue());
//					continue;
//				}
//			}
//			exInfo.setErrorMessage(infos.get("ERROR_CONTENT"));
//		}
//		return exInfo;
//	}

	public String getCurrentTime() {
		SimpleDateFormat formatter=new SimpleDateFormat(pattern);
		return formatter.format(new Date());
	}
	
	private Date parseString2Date(String date) {
		SimpleDateFormat formatter=new SimpleDateFormat(pattern);
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 处理获取到的异常信息
	 * 
	 * @param doMore
	 */
	public void handleExceptionData(GlobalExceptionInterfaces.IDoMore doMore) {
		this.doMore = doMore;
	}
	
//	/**
//	 * 获取设备信息
//	 *
//	 * @return
//	 */
//	public Map<String, String> getInfos() {
//		return infos;
//	}

	/**
	 * 获取当前设置的日期格式
	 * @return
	 */
	public String getPattern() {
		return pattern;
	}
	
	/**
	 * 设置日期格式
	 * @param pattern
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @author LuoJ
	 * @date 2013-9-17
	 * @package com.lidroid.xutils.exception-ExceptionCatcher.java
	 * @Description 扩展接口
	 */
	public static class GlobalExceptionInterfaces {
		public interface IDoMore {
			void onException(Throwable ex);
		}
	}
}