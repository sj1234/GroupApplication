package com.example.sjeong.groupapplication;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyReceiver extends BroadcastReceiver {

    private Context callcontext;
    public static TelephonyManager TelMag;
    private PhoneStateListener phonelistener;
    private static String callstate;
    private String Tag = "test MyReciver";
    private PopupActivity pop;
    private  HandleRing handleRing;
    private static PowerManager.WakeLock sCpuWakeLock;


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        callcontext = context;
        Log.i(Tag, "call receiver start");
        Toast.makeText(context, "receiver start", Toast.LENGTH_SHORT).show();

        // 브로드 캐스트 같은 상황에 여러 번 수신되는 경우
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (state.equals(callstate))
            return;
        else
            callstate = state;

        // 모드에 따라 수신여부 결정하는 부분 추가!!!!
        TelMag = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        ////////
        handleRing = new HandleRing(callcontext);

        // 팝업
        pop = new PopupActivity();

        // 전화 수신 리스너
        phonelistener = new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING: // 전화 수신
                        Log.i(Tag, "call CALL_STATE_RINGING");

                        // 1이면 즐겨찾기 2이면 즐겨찾기 외에 저장된 번호 3이면 모르는 번호
                        int contactNumber = getContacts(incomingNumber);
                        // 부재중전화 횟수
                        int calllogCount = getCallLog(incomingNumber);
                        // 모드에 따라 수신음 결과 분석
                        //ReceiveAnalysisResult(contactNumber, calllogCount);

                        if(calllogCount>100)
                            EndCall(); // 전화 종료
                        else {
                            //SharedPreferences sharedPreferences = callcontext.getSharedPreferences("AlarmCall", callcontext.MODE_PRIVATE);
                            //if(sharedPreferences.getString("LaterCall", "not found").equals("ON"))
                            Popup(incomingNumber);// 팝업
                        }

                        // 수신음 변경
                        if(contactNumber==0) {
                            handleRing.changeRing(3);
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE: // 전화 종료
                        Log.i(Tag, "call CALL_STATE_IDLE");
                        // 팝업 종료
                        if(PopupActivity.pcontext!=null) {
                            ((PopupActivity)pop.pcontext).EndPopup();
                        }
                        // 수신음 되돌리기
                        handleRing.changeRing(4);
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:  // 전화 중
                        Log.i(Tag, "call CALL_STATE_OFFHOOK");
                        // 팝업 종료
                        if(PopupActivity.pcontext!=null) {
                            ((PopupActivity)pop.pcontext).EndPopup();
                        }
                        break;
                    default:
                        break;
                }
                TelMag.listen(phonelistener, PhoneStateListener.LISTEN_NONE);
            }
        };

        TelMag.listen(phonelistener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    // 전화번호부 정보 확인
    private int getContacts(String phoneNumber){

        Log.i(Tag, "call receiver contacts " + phoneNumber);
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        String[] selectionArgs = null;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.STARRED,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        };

        Cursor cursor = callcontext.getContentResolver().query(uri, projection, null, selectionArgs, sortOrder);

        if (cursor.moveToFirst()) {
            do {
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String star = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED));
                number = number.replace(" ", "");
                number = number.replace("-", "");
                if (phoneNumber.equals(number)) {
                    if (star.equals("1")) {
                        Log.i(Tag, "star Number " + star + " / " + phoneNumber);
                        return 1;
                    } // 즐겨찾기
                    else {
                        Toast.makeText(callcontext, "있는번호", Toast.LENGTH_SHORT).show();
                        Log.i(Tag, "know Number " + phoneNumber);
                        return 2;
                    } // 전화번호부에 있는 번호
                }
            } while (cursor.moveToNext());

            // 없는 번호
            Toast.makeText(callcontext, "없는번호", Toast.LENGTH_SHORT).show();
            Log.i(Tag, "unknow Number");
            return 3;
        } else
            Toast.makeText(callcontext, "주소록에 번호가 없습니다.", Toast.LENGTH_SHORT).show();
            return 0; // 주소록에 번호가 없는 경우

    }

    // 5분전에온 부재중 전화 횟수
    private int getCallLog(String phoneNumber) {
        int count = 0; // 부재중 횟수
        Calendar calendar;

        Uri uri = CallLog.Calls.CONTENT_URI;
        String[] projection = new String[]{
                CallLog.Calls.TYPE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION
        };
        String selection = CallLog.Calls.TYPE +"!=2";

        // 부재중전화 권한 확인 (후에 작성해야 할 부분!!)
        if (ActivityCompat.checkSelfPermission(callcontext, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i(Tag, "부재중 전화 권한!!");
            return 0;
        }
        else {
            Cursor cursor = callcontext.getContentResolver().query(uri, projection, selection, null, null);

            calendar = Calendar.getInstance(); // 현재시간
            calendar.add(Calendar.MINUTE, -5);  // 현재시간 5분전
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
            Log.i(Tag, "stringdate " + dateformat.format(calendar.getTime())); // 5분전 시간 출력

            if (cursor.moveToFirst()) {
                do {
                    String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    number = number.replace(" ", "");
                    number = number.replace("-", "");
                    long duration=  cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION));
                    String type = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
                    if (phoneNumber.equals(number) && (duration==0 || type.equals("3"))){
                        long longcalldate = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
                        Date calldate = new Date(longcalldate);

                        if(calendar.getTime().before(calldate)) { // 5분 전 부재중 전화 수
                            Log.i(Tag, "이전기록만 " + dateformat.format(calldate)+" type : "+type);
                            count++;
                        }
                    }
                } while (cursor.moveToNext());
            }

            Log.i(Tag, "부재중 수 " +count);
            return count;
        }
    }

    // 전화 정보 팝업 띄우기
    public void Popup(String number){

        Intent popupintent= new Intent(callcontext,PopupActivity.class);
        popupintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        popupintent.putExtra("phonenumber", number);

        PendingIntent pendingintent=PendingIntent.getActivity(callcontext, 0, popupintent, PendingIntent.FLAG_ONE_SHOT);
        try{
            pendingintent.send();
        }
        catch (Exception e){
            e.printStackTrace();
            Log.i(Tag, "Popup error");
            Toast.makeText(callcontext, e.toString(), Toast.LENGTH_LONG);
        }
    }

    //통화종료
    private void EndCall(){
        try {
            Class c = Class.forName(TelMag.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            ITelephony telephonyService = (ITelephony) m.invoke(TelMag);
            telephonyService.endCall();

            Log.i(Tag,"end call");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



