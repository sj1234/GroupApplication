package com.example.sjeong.groupapplication;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyReceiver extends BroadcastReceiver {

    private TelephonyManager TelMag;
    private PhoneStateListener phonlistener;
    private Context callcontext;
    private static String callstate;
    private String Tag = "test MyReciver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        callcontext = context;
        Log.i(Tag, "call receiver start");
        Toast.makeText(context, "receiver start", Toast.LENGTH_SHORT).show();

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (state.equals(callstate))
            return;
        else
            callstate = state;

        // 모드에 따라 수신여부 결정하는 부분 추가!!!!

        TelMag = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        phonlistener = new phoneListener();
        TelMag.listen(phonlistener, PhoneStateListener.LISTEN_CALL_STATE);
    }


    // 전화 수신 리스너
    public class phoneListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.i(Tag, "call CALL_STATE_RINGING");

                    // 1이면 즐겨찾기 2이면 즐겨찾기 외에 저장된 번호 3이면 모르는 번호
                    int contactNumber = getContacts(incomingNumber);
                    // 부재중전화 횟수
                    int calllogCount = getCallLog(incomingNumber);
                    // 모드에 따라 수신음 결과 분석
                    ReceiveAnalysisResult(contactNumber, calllogCount, state);
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.i(Tag, "call CALL_STATE_IDLE");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.i(Tag, "call CALL_STATE_OFFHOOK");
                    break;
                default:
                    break;
            }
            TelMag.listen(phonlistener, PhoneStateListener.LISTEN_NONE);
        }
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
                CallLog.Calls.DATE
        };
        String selection = CallLog.Calls.TYPE+"=3";

        if (ActivityCompat.checkSelfPermission(callcontext, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
                    if (phoneNumber.equals(number)){
                        long longcalldate = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
                        Date calldate = new Date(longcalldate);

                        if(calendar.getTime().before(calldate)) { // 5분 전 부재중 전화 수
                            Log.i(Tag, "이전기록만 " + dateformat.format(calldate));
                            count++;
                        }
                    }
                } while (cursor.moveToNext());
            }

            Log.i(Tag, "부재중 수 " +count);
            return count;
        }
    }


    // 벨소리 설정 정보 넘기기
    private void ReceiveAnalysisResult(int contactNumber, int calllogCount, int state){
        // contactNumber = 1이면 즐겨찾기 2이면 즐겨찾기 외에 저장된 번호 3이면 모르는 번호
        // calllogCount = 부재중전화 횟수

        // 부재중이 4이면 무음으로 만들기
        if(calllogCount==4) {
            handleRing handle = new handleRing(callcontext, 3, state);
        }
    }
}
