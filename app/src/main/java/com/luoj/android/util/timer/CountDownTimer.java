package com.luoj.android.util.timer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author LuoJ
 * @date 2014-12-15
 * @package j.android.library.utils.timer -- CountDownTimer.java
 * @Description 
 */
public class CountDownTimer extends TimerTask{

	private Timer mTimer=new Timer();

    public int countDownNum;

	public CountDownTimer(int countDownNum) {
		this.countDownNum = countDownNum;
	}

	@Override
	public void run() {
		if (countDownNum<1) {
			if(null!=mCountDownCallback)mCountDownCallback.onTimerEnd();
			mTimer.cancel();
			return;
		}
		if(null!=mCountDownCallback)mCountDownCallback.onTimerRunning(countDownNum);
		countDownNum--;
	}
	
	public void start(){
		mTimer.schedule(this, 0, 1000);
	}
	
	public void stop(){
		mTimer.cancel();
	}
	
	public void setTimerListener(CountDownCallback countDownCallback){
		this.mCountDownCallback=countDownCallback;
	}
	private CountDownCallback mCountDownCallback;
	public interface CountDownCallback{
		void onTimerRunning(int residue);
		void onTimerEnd();
	}
	
}


