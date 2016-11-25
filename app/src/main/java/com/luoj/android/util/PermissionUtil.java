package com.luoj.android.util;

/**
 * Created by 京 on 2016/6/8.
 */
public class PermissionUtil {

//    /** * 检测系统弹出权限 * @param cxt * @param req * @return */
//    @TargetApi(23)
//    public static boolean checkSettingAlertPermission(Object cxt,int req) {
//        if (cxt instanceof Activity) {
//            Activity activity = (Activity) cxt;
//            if (!Settings.canDrawOverlays(activity.getBaseContext())) {
//                LogUtil.i("Setting not permission");
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                        Uri.parse("package:" + activity.getPackageName()));
//                activity.startActivityForResult(intent, req);
//                return false;
//            }
//        } else if (cxt instanceof Fragment) {
//            Fragment fragment = (Fragment) cxt;
//            if (!Settings.canDrawOverlays(fragment.getActivity())) {
//                LogUtil.i("Setting not permission");
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                        Uri.parse("package:" + fragment.getActivity().getPackageName()));
//                fragment.startActivityForResult(intent, req);
//                return false;
//            }
//        } else {
//            throw new RuntimeException("cxt is net a activity or fragment");
//        }
//        return true;
//    }

}
