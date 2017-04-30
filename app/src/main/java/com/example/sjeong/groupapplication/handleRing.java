package com.example.sjeong.groupapplication;

import android.content.Context;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class handleRing {
    Context mContext; // context를 ChangeRing class에 넘김 (activity 없이 getSystemService 사용)
    AudioManager myAudioManager;
    TelephonyManager myTelephonyManager;
    PhoneStateListener phListner= new PhoneStateListener();
    public void onCallStateChanged(int state, String incomingNumber){
        phListner.onCallStateChanged(state, incomingNumber);
    }


    public handleRing(Context mContext, int i, int state) {
        this.mContext = mContext;
        int ringerMode=getRing(i); // 현재 착신음 저장
        changeRing(i);  // 착신음 변경
        while(state != TelephonyManager.CALL_STATE_IDLE);
        //통화 종료 후 기존 설정으로 돌아가기.
        setRing(ringerMode);
    }

    public int getRing(int i) {
        myAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        myTelephonyManager=(TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
        int ringerMode= myAudioManager.getRingerMode();
        return ringerMode;
    }

    public void setRing(int ringerMode){
        // 기존 설정으로 돌아가기.
        myAudioManager.setRingerMode(ringerMode);
    }


    public void ring() { //1:RINGER_MODE_VIBRATE 2:RINGER_MODE_NORMAL 0:RINGER_MODE_SILENT
        // 벨소리로 변경;
        myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }
    public void vibrate() {
        // 진동으로 변경;
        myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
    }
    public void silent() {
        // 무음으로 변경;
        myAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

    public void changeRing(int i) {
        // int i 가 0이면 통화종료, 1이면 벨소리, 2이면 진동, 3이면 무음
        switch (i) {
            case 0:
                //통화종료();
                break;
            case 1:
                ring();
                break;
            case 2:
                vibrate();
                break;
            case 3:
                silent();
                break;
            default:
                System.out.print("해당 착신음 모드가 없습니다.");
        }
    }
}
