package com.example.sjeong.groupapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PopupActivity extends AppCompatActivity implements View.OnClickListener{

    private static String Tag = "test Popup";
    private String number;
    public static Context pcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE); // 상단 바 없에기
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_popup);
        Log.i(Tag,"Open Popup");

        pcontext = this; // context 정보 저장

        // 전화번호 정보 받아오기
        Intent intent = getIntent();
        number = intent.getStringExtra("phonenumber");

        // 팝업 사이즈 설정
        Display display = ((WindowManager) getSystemService(this.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int) (display.getWidth() * 0.7); //Display 사이즈의 70%
        int height = (int) (display.getHeight() * 0.5);  //Display 사이즈의 20%
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        //종료버튼
        Button button = (Button) this.findViewById(R.id.close);
        button.setOnClickListener((View.OnClickListener) this);

        //나중에 알림
        Button later = (Button) this.findViewById(R.id.later);
        later.setOnClickListener((View.OnClickListener) this);

        // 전화번호 띄우기
        TextView phonenumber = (TextView) findViewById(R.id.txt_phonenumber);
        phonenumber.setText(number);

    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.close:
                EndPopup();
                break;
            case R.id.later:
                // 나중에 알림 저장!!
                LaterCallAlarm();
                EndCall();
                break;
            default:
                break;
        }
    }

    // 팝업 종료
    public void EndPopup(){
        PopupActivity.this.finish();
    }

    // 통화 차단
    private void EndCall(){
        try {

            Class c = Class.forName(MyReceiver.TelMag.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            ITelephony telephonyService = (ITelephony) m.invoke(MyReceiver.TelMag);
            telephonyService.endCall();
            Log.i(Tag,"end call");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 나중에 알람이 오도록
    private void LaterCallAlarm(){
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        Intent alarmintent= new Intent(PopupActivity.this, LaterCall.class);
        alarmintent.putExtra("phonenumber", number); // 전화번호 정보 전달
        PendingIntent pendingintent=PendingIntent.getBroadcast(PopupActivity.this, 0, alarmintent, PendingIntent.FLAG_ONE_SHOT);

        Calendar calendar = Calendar.getInstance(); // 현재시간
        calendar.add(Calendar.SECOND, 15);  // 현재시간 10분 후 (test는 15초 후로)

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
        Log.i(Tag, "alarmtime " + dateformat.format(calendar.getTime())); // 시간 출력

        //RTC_WAKEUP : 지금 시간을 기준으로 알람이 동작, sleep모드여도 실행한다.
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingintent);
        Log.i(Tag,"send later call alarm");
    }
}
