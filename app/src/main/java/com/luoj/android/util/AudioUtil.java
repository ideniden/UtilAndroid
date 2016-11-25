package com.luoj.android.util;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;

/**
 * Created by LuoJ on 2015/1/19.
 */
public class AudioUtil {

    public static void setSpeakerphoneOn(Activity activity,boolean on) {
        AudioManager audioManager = (AudioManager)activity.getSystemService(Context.AUDIO_SERVICE);
        if(on) {
            audioManager.setSpeakerphoneOn(true);
        } else {
            audioManager.setSpeakerphoneOn(false);//关闭扬声器
            audioManager.setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL);
            activity.setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            //把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_CALL);
        }
    }

    /**
     * 自动设置声音输出设备.
     * 如果有蓝牙耳机、耳机切换到耳机，如果都没有默认为扬声器
     */
    public static int autoSetOutputDevice(Activity activity){
        AudioManager mAudioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        if (mAudioManager.isBluetoothA2dpOn()) {
            setSpeakerphoneOn(activity, false);
            LogUtil.d("当前为蓝牙A2dp");
        }else if (mAudioManager.isBluetoothScoOn()) {
            setSpeakerphoneOn(activity, false);
            LogUtil.d("当前为蓝牙Sco");
        }else if (mAudioManager.isWiredHeadsetOn()) {
            setSpeakerphoneOn(activity, false);
            LogUtil.d("当前为耳机");
        }else {
            setSpeakerphoneOn(activity, true);
            LogUtil.d("当前为扬声器");
        }
        LogUtil.d(String.format("当前各状态Bluetooth_sco->%s,Bluetooth_a2dp->%s,WiredHeadset->%s,Speakerphone->%s",mAudioManager.isBluetoothScoOn(),mAudioManager.isBluetoothA2dpOn(),mAudioManager.isWiredHeadsetOn(),mAudioManager.isSpeakerphoneOn()));
        return getAudioOutputType(activity);
    }

    public static final int TYPE_AUDIO_OUTPUT_BLUETOOTHA2DP=1;
    public static final int TYPE_AUDIO_OUTPUT_BLUETOOTHSCO=2;
    public static final int TYPE_AUDIO_OUTPUT_WIREDHEADSET=3;
    public static final int TYPE_AUDIO_OUTPUT_TELEPHONE_RECEIVER=4;
    public static final int TYPE_AUDIO_OUTPUT_SPEAKER=5;

    public static int getAudioOutputType(Activity activity){
        int i=TYPE_AUDIO_OUTPUT_TELEPHONE_RECEIVER;
        AudioManager mAudioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        if (mAudioManager.isBluetoothA2dpOn()) {
            i=TYPE_AUDIO_OUTPUT_BLUETOOTHA2DP;
            LogUtil.d("当前为蓝牙A2dp");
        }else if (mAudioManager.isBluetoothScoOn()) {
            i=TYPE_AUDIO_OUTPUT_BLUETOOTHSCO;
            LogUtil.d("当前为蓝牙Sco");
        }else if (mAudioManager.isWiredHeadsetOn()) {
            i=TYPE_AUDIO_OUTPUT_WIREDHEADSET;
            LogUtil.d("当前为耳机");
        }else if (mAudioManager.isSpeakerphoneOn()){
            i=TYPE_AUDIO_OUTPUT_SPEAKER;
            LogUtil.d("当前为扬声器");
        }
        return i;
    }

}
