package com.luoj.android.util;


import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;


import com.luoj.android.util.activity.NotificationReturnActivity;

import java.io.File;

/**
 * @author LuoJ
 * @date 2014-9-4
 * @package j.android.library.utils -- IntentUtil.java
 * @Description intent工具类，提供一些通用跳转接口，以及规范跳转
 */
public class IntentUtil {

//	public static void startActivity(Activity activity, Class<?> cls, BasicNameValuePair... name) {
//		Intent intent = new Intent();
//		intent.setClass(activity, cls);
//		for (int i = 0; i < name.length; i++) {
//			intent.putExtra(name[i].getName(), name[i].getValue());
//		}
//		activity.startActivity(intent);
//		activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//	}

    public static void installApk(Context ctx, String filePath) {
        installApk(ctx, new File(filePath));
    }

    public static void installApk(Context ctx, File apkfile) {
        if (!apkfile.exists()) {
        } else {
            ctx.startActivity(getInstallApkIntent(apkfile));
        }
    }

    public static Intent getInstallApkIntent(String filePath) {
        return getInstallApkIntent(new File(filePath));
    }

    public static Intent getInstallApkIntent(File apkfile) {
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setAction(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        return i;
    }

    public static void redirectWithBrowser(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    public static void startLauncher(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(intent);
    }

    public static void startInstalledAppDetails(Context context) {
        startInstalledAppDetails(context, context.getPackageName());
    }

    public static void startInstalledAppDetails(Context context, String packageName) {
        Intent intent = new Intent();
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= 9) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", packageName, null));
        } else {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra((sdkVersion == 8 ? "pkg" : "com.android.settings.ApplicationPkgName"), packageName);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static PendingIntent getCanBackApplicationPendingIntent(Context ctx) {
        return getCanBackApplicationPendingIntent(ctx, NotificationReturnActivity.class);
    }

    public static PendingIntent getCanBackApplicationPendingIntent(Context ctx, Class<? extends Activity> activityClz) {
        return getCanBackApplicationPendingIntent(ctx, activityClz, null);
    }

    public static PendingIntent getCanBackApplicationPendingIntent(Context ctx, Intent intent) {
        return getCanBackApplicationPendingIntent(ctx, null, intent);
    }

    public static PendingIntent getCanBackApplicationPendingIntent(Context ctx, Class<? extends Activity> activityClz, Intent intent) {
        if (null == intent) {
            intent = new Intent(ctx, activityClz);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 分享
     * @param activity
     * @param subjectString
     * @param contentString
     * @param uri
     */
    public static void sendShare(Activity activity, String subjectString, String contentString, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, subjectString);
        intent.putExtra(Intent.EXTRA_TEXT, contentString);
        if (null != uri) intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(Intent.createChooser(intent, activity.getTitle()));
    }

    public static void sendShare(Activity activity, String subjectString, String contentString) {
        sendShare(activity, subjectString, contentString, null);
    }

    public static void sendEmail(Activity activity, String subjectString, String contentString) {
        sendEmail(activity, "", subjectString, contentString);
    }

    public static void sendEmail(Activity activity, String address, String subjectString, String contentString) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
//        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_SUBJECT, subjectString);
        intent.putExtra(Intent.EXTRA_TEXT, contentString);
//        intent.setData(Uri.parse("mailto:receiver@mailserver.com"));
        intent.setData(Uri.parse("mailto:" + address));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(Intent.createChooser(intent, activity.getTitle()));
    }

    public static void sendMessage(Activity activity, String address, String message) {
        Uri smsToUri = Uri.parse(TextUtils.isEmpty(address) ? "smsto:" : "smsto://" + address);
        Intent mIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        mIntent.putExtra("sms_body", message);
        activity.startActivity(mIntent);
    }

    public static void call(Activity activity, String telNum) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telNum));
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        activity.startActivity(intent);
    }

    public static void addShortcut(Context ctx,Class activityClz,String name) {
        Intent addShortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重复创建
        addShortcutIntent.putExtra("duplicate", false);// 经测试不是根据快捷方式的名字判断重复的
        // 应该是根据快链的Intent来判断是否重复的,即Intent.EXTRA_SHORTCUT_INTENT字段的value
        // 但是名称不同时，虽然有的手机系统会显示Toast提示重复，仍然会建立快链
        // 屏幕上没有空间时会提示
        // 注意：重复创建的行为MIUI和三星手机上不太一样，小米上似乎不能重复创建快捷方式
        // 名字
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        // 图标
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(ctx,
                        R.mipmap.ic_launcher));
        // 设置关联程序
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.setClass(ctx, activityClz);
//        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        launcherIntent.addCategory(Intent.CATEGORY_DEFAULT);
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);
        // 发送广播
        ctx.sendBroadcast(addShortcutIntent);
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putBoolean("hasInstallShortcut_"+name,true).commit();
    }

    public static void removeShortcut(Context ctx,Class activityClz,String name) {
        // remove shortcut的方法在小米系统上不管用，在三星上可以移除
        Intent intent = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        // 名字
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        // 设置关联程序
        Intent launcherIntent = new Intent(ctx,activityClz);
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);
        // 发送广播
        ctx.sendBroadcast(intent);
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putBoolean("hasInstallShortcut_" + name, false).commit();
    }

    public static boolean hasInstallShortcut(Context ctx,String name) {
        boolean hasInstall = false;
//        final String AUTHORITY = "com.android.launcher2.settings";
//        Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
//                + "/favorites?notify=true");
//        // 这里总是failed to find provider info
//        // com.android.launcher2.settings和com.android.launcher.settings都不行
//        Cursor cursor = ctx.getContentResolver().query(CONTENT_URI,
//                new String[]{"title", "iconResource"}, "title=?",
//                new String[]{name}, null);
//        if (cursor != null && cursor.getCount() > 0) {
//            hasInstall = true;
//        }
        hasInstall= PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean("hasInstallShortcut_"+name,false);
        return hasInstall;
    }

}
