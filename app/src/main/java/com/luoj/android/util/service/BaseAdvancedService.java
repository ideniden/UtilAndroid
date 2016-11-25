package com.luoj.android.util.service;

import com.luoj.android.util.AppUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 京 on 2016/7/28.
 */
public abstract class BaseAdvancedService extends BaseService{

    boolean backForeground=true;//启动应用时不触发前台回调
    Timer timer;
    TimerTask timerTask=new TimerTask() {
        @Override
        public void run() {
            if(AppUtil.isAction(BaseAdvancedService.this)){
                if(!backForeground){
                    onAppForeground();
                }
                backForeground=true;
            }else{
                if(backForeground){
                    onAppBackground();
                }
                backForeground=false;
            }
        }
    };

    protected abstract void onAppForeground();

    protected abstract void onAppBackground();

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        timer.schedule(timerTask,0,1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.purge();
        timerTask.cancel();
        timer=null;
        timerTask=null;
    }

}
