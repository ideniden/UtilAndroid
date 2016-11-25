package com.luoj.android.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by LuoJ on 2015/1/6.
 */
public class InputMethodUtil {

    /**
     * 显示输入法.
     * 可用
     * @param ctx
     * @param v
     */
    public static void showInputMethod(Context ctx,View v){
        InputMethodManager imm = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v,InputMethodManager.SHOW_FORCED);
    }

    public static void showImplicitInputMethod(Context ctx,View v){
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 显示输入法.
     * 可用
     * @param activity
     */
    public static void showInputMethod(Activity activity){
        showInputMethod(activity,activity.getCurrentFocus());
    }

    /**
     * 隐藏输入法.
     * 可用
     * @param ctx
     * @param v
     */
    public static void dismissInputMethod(Context ctx,View v){
        InputMethodManager inputMethodManager = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

    /**
     * 隐藏输入法.
     * 可用
     * @param activity
     */
    public static void dismissInputMethod(Activity activity){
        dismissInputMethod(activity,activity.getCurrentFocus());
    }

    /**
     * 开关输入法.
     * 可用
     * @param ctx
     */
    public static void toggleInputMethod(Context ctx){
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 输入法是否显示.
     * 待测
     * @param ctx
     * @return
     */
    public static boolean isShow(Context ctx){
        InputMethodManager imm = ( InputMethodManager ) ctx.getSystemService( Context.INPUT_METHOD_SERVICE );
        return imm.isActive();
    }

}
