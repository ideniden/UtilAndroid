package com.luoj.android.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author LuoJ
 * @date 2013-9-23
 * @package j.android.library.utils -- PhoneUtils.java
 * @Description 手机相关信息获取的工具类
 */
public class PhoneUtil {

    private static TelephonyManager mTelephoneManage;

    /**
     * 获取电话管理类实例
     *
     * @param mctx
     * @return
     */
    private static TelephonyManager getTelephoneyManager(Context mctx) {
        if (null == mTelephoneManage) {
            mTelephoneManage = (TelephonyManager) mctx.getSystemService(Context.TELEPHONY_SERVICE);
        }
        return mTelephoneManage;
    }

    public static String getPhoneIMSI(Context mctx){
        getTelephoneyManager(mctx);
        return mTelephoneManage.getSubscriberId();
    }

    /**
     * 获取运营商名称
     *
     * @param mctx
     * @return
     */
    public static String getProvidersName(Context mctx) {
        getTelephoneyManager(mctx);
        String ProvidersName = null;
        String IMSI = mTelephoneManage.getSubscriberId();
//        LogUtil.e("IMSI-->" + IMSI);
        if (null == IMSI) {
            return ProvidersName;
        }
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
            ProvidersName = "中国移动";
        } else if (IMSI.startsWith("46001")) {
            ProvidersName = "中国联通";
        } else if (IMSI.startsWith("46003")) {
            ProvidersName = "中国电信";
        }
        return ProvidersName;
    }

    /**
     * 获取手机的DeviceId（设备号）
     *
     * @param mctx
     * @return
     */
    public static String getPhoneIMEI(Context mctx) {
        getTelephoneyManager(mctx);
        return mTelephoneManage.getDeviceId();
    }

    /**
     * 获取手机的IP地址
     *
     * @return
     */
    public static String getPhoneIP() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    public static String getPhoneNumber(Context mctx){
        getTelephoneyManager(mctx);
        return mTelephoneManage.getSimSerialNumber();
    }

    /**
     * 获取设备相关信息
     *
     * @param infos
     */
    public static void getDeviceInfo(Map<String, String> infos) {
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                // LogUtils.e(field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                LogUtil.e("an error occured when collect crash info", e);
            }
        }
    }

    public static DeviceInfo getDeviceInfo(Context ctx) {
        DeviceInfo deviceInfo = null;
        Map<String, String> infos = new HashMap<String, String>();
        getDeviceInfo(infos);
        if (!infos.isEmpty() && infos.size() > 0) {
            deviceInfo = new DeviceInfo(infos);
        }
        deviceInfo.androidId=Settings.System.getString(ctx.getContentResolver(), Settings.System.ANDROID_ID);
        DisplayMetrics dm=ctx.getResources().getDisplayMetrics();
        deviceInfo.phoneNumber=getPhoneNumber(ctx);
        deviceInfo.width = dm.widthPixels;
        deviceInfo.height = dm.heightPixels;
        deviceInfo.density=dm.density;
        deviceInfo.densityDpi=dm.densityDpi;
        deviceInfo.scaledDensity=dm.scaledDensity;

        deviceInfo.langType = Locale.getDefault().getLanguage();
        deviceInfo.timeZone = getTimeZoneWithGMT();
        deviceInfo.netInfo = NetStatusUtil.getNetworkType(ctx);
        deviceInfo.imsi=getTelephoneyManager(ctx).getSubscriberId();
        deviceInfo.provider=getProvidersName(ctx);
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mInfo);
        deviceInfo.availMem = ((mInfo.availMem >> 10) + "k");
        if (Build.VERSION.SDK_INT >= 16) {
            deviceInfo.totalMem = String.valueOf(mInfo.totalMem);
        }
        deviceInfo.threshold = String.valueOf(mInfo.threshold);
        deviceInfo.lowMemory = mInfo.lowMemory;
        return deviceInfo;
    }

    public static String getTimeZoneWithGMT(){
        return "GMT+" + (Calendar.getInstance().getTimeZone().getRawOffset()/3600000);
    }

    //
    // private String currentRuntimeValue() {
    // return SystemProperties.get(SELECT_RUNTIME_PROPERTY,
    // VMRuntime.getRuntime().vmLibrary());
    // }

    public static boolean isSystemInChinese() {
        return "zh".equals(getPhoneLanguage());
    }

    public static boolean isSystemInChineseEn() {
        return "en".equals(getPhoneLanguage());
    }

    public static boolean isSystemInChineseTw() {
        String localLanguage = Locale.getDefault().toString();
        return "zh_TW".equals(localLanguage);
    }

    public static String getPhoneLanguage() {// zh
        return Locale.getDefault().getLanguage();
    }


    public static void setLanguage(Context ctx, Locale locale) {
        Resources resources = ctx.getResources();//获得res资源对象
        Configuration config = resources.getConfiguration();//获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等。
        config.locale = locale; //语言
        resources.updateConfiguration(config, dm);
    }

    /**
     * 点亮屏幕并且解锁
     *
     * @param ctx
     * @param callback
     */
    public static void lightScreenAndDisableKeyguard(Context ctx, LightScreenCallback callback) {
        PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
        // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        // 点亮屏幕
        wl.acquire();

        // 得到键盘锁管理器对象
        KeyguardManager km = (KeyguardManager) ctx.getSystemService(Context.KEYGUARD_SERVICE);
        // 参数是LogCat里用的Tag
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        try {
            // 解锁
            kl.disableKeyguard();
        } catch (Exception e) {
        }
        /*
		 * 这里写程序的其他代码
		 */
        if (null != callback)
            callback.onScreenLight();

        try {
            // 重新启用自动加锁
            kl.reenableKeyguard();
        } catch (Exception e) {
        }
        // 释放
        wl.release();
    }

    public interface LightScreenCallback {
        void onScreenLight();
    }

    /**
     * 沉浸模式. 可以隐藏通知栏导航栏，并且通知栏色值随应用标题栏 系统必须4.4以上才支持
     *
     * @param activity
     */
    public static void immerseMode(Activity activity) {
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_FULLSCREEN);// |
            // View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }

    /**
     * 隐藏虚拟按键栏. 必须在setContentView之前调用 如果需要完全全屏，可以在manifest中加入：android:theme=
     * "@android:style/Theme.NoTitleBar.Fullscreen"
     *
     * @param activity
     */
    public static void hideNavigationBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= 14) {
            Window window = activity.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE;
            window.setAttributes(params);
        }
    }

    public static void hideTitleBar(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void showTitleBar(Activity activity) {
        activity.getWindow().setFlags(~WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void copyText(Context ctx, String text) {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt > Build.VERSION_CODES.HONEYCOMB) {// api11
            ClipboardManager copy = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            copy.setText(text);
        } else if (sdkInt <= Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager copyq = (android.text.ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            copyq.setText(text);
        }
    }

    public static void copyText(Context ctx, String text, String toast) {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt > Build.VERSION_CODES.HONEYCOMB) {// api11
            ClipboardManager copy = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            copy.setText(text);
            ToastUtil.showToast(ctx, toast);
        } else if (sdkInt <= Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager copyq = (android.text.ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            copyq.setText(text);
            ToastUtil.showToast(ctx, toast);
        }
    }

    /**
     * 获取屏幕宽度和高度，单位为px
     *
     * @param context
     * @return
     */
    public static Point getScreenMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        LogUtil.d("Screen---Width = " + w_screen + " Height = " + h_screen + " densityDpi = " + dm.densityDpi);
        return new Point(w_screen, h_screen);
    }

    public static int getStatusBarHeight(Activity activity){
        Rect rect=new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    /**
     * 获取屏幕长宽比
     *
     * @param context
     * @return
     */
    public static float getScreenPortraitRate(Context context) {
        Point P = getScreenMetrics(context);
        float H = P.y;
        float W = P.x;
        return (H / W);
    }

    public static float getScreenLandscapeRate(Context context) {
        Point P = getScreenMetrics(context);
        float H = P.y;
        float W = P.x;
        return (H / W);
    }

    // @TargetApi(14)
    // public static boolean isBlueToothHeadsetConnected() {
    // boolean retval = true;
    // try {
    // retval =
    // BluetoothAdapter.getDefaultAdapter().getProfileConnectionState(android.bluetooth.BluetoothProfile.HEADSET)
    // != android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;
    //
    // } catch (Exception exc) {
    // // nothing to do
    // }
    // return retval;
    // }

//    public static List<ContactInfo> getLocalContactsList(Context ctx) {
//        DebugUtil.MethodExecTimeTrace debug = DebugUtil.startMethodExecTimeTrace();
//        List<ContactInfo> datas = null;
//        ContentResolver resolver = ctx.getContentResolver();
//        // 获取手机联系人
//        String[] PHONES_PROJECTION = new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
//        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
//        if (phoneCursor != null) {
//            datas = new ArrayList<ContactInfo>();
//            while (phoneCursor.moveToNext()) {
//                //得到联系人ID
//                Long id = phoneCursor.getLong(0);
//                //得到联系人名称
//                String name = phoneCursor.getString(1);
//                //得到手机号码
//                String number = phoneCursor.getString(2);
//                String email = "";
////				Cursor emailCursor = ctx.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
////						null,
////						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
////								+ " = " + id, null, null);
////				if(null!=emailCursor&&emailCursor.moveToFirst()){
////					// 遍历所有的电话号码
////					String emailType = emailCursor.getString(emailCursor
////							.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
////					String emailValue = emailCursor
////							.getString(emailCursor
////									.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
////					email=emailType+"-"+emailValue;
////				}
////				LogUtil.d(String.format("id->%s,name->%s,number->%s,email->%s\n", id + "", name, number,email));
//                datas.add(new ContactInfo(id, name, email, number));
//            }
//            phoneCursor.close();
//        }
//        debug.stopTrace("xinglocalcontacts");
//        return datas;
//    }

    public static String getWifiIp(Context ctx) {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            return null;
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int i = wifiInfo.getIpAddress();
        return (i & 0xFF ) + "." +
                        ((i >> 8 ) & 0xFF) + "." +
                        ((i >> 16 ) & 0xFF) + "." +
                        ( i >> 24 & 0xFF) ;

    }

    public static String getPhoneName(){
        return "";
    }
    public static class DeviceInfo {
        public Long id;//
        public String androidId;
        public String phoneNumber;
        public int appVersionCode;
        public String appVersionName;
        public String imei;//设备号
        public String imsi;
        public String provider;
        public String board;//
        public String brand;//厂商、商标，牌子
        public String cpuAbi;//处理器架构，例如x86
        public String cpuAbi2;//处理器类型，例如armeabi-v7a
        public String device;//
        public String display;//
        public int width;//屏幕宽
        public int height;//屏幕高
        public float density;//
        public int densityDpi;
        public float scaledDensity;
        public String fingerprint;//
        public String hardware;//型号,例如mx3
        public String host;//
        public String hostId;//
        public String isDebuggable;//
        public String manufacturer;//
        public String model;//
        public String product;//
        public String radio;//
        public String serial;//
        public String tags;//
        public String type;//
        public String unkonwn;//
        public String langType;//语言,ch中文en英文等，用英文值
        public String timeZone;//客户端使用的时区
        public String netInfo;//是否是使用wifi
        public String availMem;
        public String totalMem;
        public String threshold;//系统内存低于此值时，会看做低内存运行
        public boolean lowMemory;
        public DeviceInfo(Map<String, String> infos) {
            this.imei = infos.get("IMEI");
            this.board = infos.get("BOARD");
            this.brand = infos.get("BRAND");
            this.cpuAbi = infos.get("CPU_ABI");
            this.cpuAbi2 = infos.get("CPU_ABI2");
            this.device = infos.get("DEVICE");
            this.display = infos.get("DISPLAY");
            this.fingerprint = infos.get("FINGERPRINT");
            this.hardware = infos.get("HARDWARE");
            this.host = infos.get("HOST");
            this.hostId = infos.get("ID");
            this.isDebuggable = infos.get("IS_DEBUGGABLE");
            this.manufacturer = infos.get("MANUFACTURER");
            this.model = infos.get("MODEL");
            this.radio = infos.get("RADIO");
            this.serial = infos.get("SERIAL");
            this.tags = infos.get("TAGS");
            this.type = infos.get("TYPE");
            this.unkonwn = infos.get("UNKNOWN");
        }

        @Override
        public String toString() {
            return "DeviceInfo{" +
                    "id=" + id +
                    ", imei='" + imei + '\'' +
                    ", imsi='" + imsi + '\'' +
                    ", provider='" + provider + '\'' +
                    ", board='" + board + '\'' +
                    ", brand='" + brand + '\'' +
                    ", cpuAbi='" + cpuAbi + '\'' +
                    ", cpuAbi2='" + cpuAbi2 + '\'' +
                    ", device='" + device + '\'' +
                    ", display='" + display + '\'' +
                    ", width=" + width +
                    ", height=" + height +
                    ", density=" + density +
                    ", densityDpi=" + densityDpi +
                    ", scaledDensity=" + scaledDensity +
                    ", fingerprint='" + fingerprint + '\'' +
                    ", hardware='" + hardware + '\'' +
                    ", host='" + host + '\'' +
                    ", hostId='" + hostId + '\'' +
                    ", isDebuggable='" + isDebuggable + '\'' +
                    ", manufacturer='" + manufacturer + '\'' +
                    ", model='" + model + '\'' +
                    ", product='" + product + '\'' +
                    ", radio='" + radio + '\'' +
                    ", serial='" + serial + '\'' +
                    ", tags='" + tags + '\'' +
                    ", type='" + type + '\'' +
                    ", unkonwn='" + unkonwn + '\'' +
                    ", langType='" + langType + '\'' +
                    ", timeZone='" + timeZone + '\'' +
                    ", netInfo='" + netInfo + '\'' +
                    ", availMem='" + availMem + '\'' +
                    ", totalMem='" + totalMem + '\'' +
                    ", threshold='" + threshold + '\'' +
                    ", lowMemory=" + lowMemory +
                    '}';
        }
    }

}
