package com.example.sjeong.groupapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


public class ScheduleActivity extends AppCompatActivity implements View.OnClickListener{

    private String Tag = "test ScheduleActivity";
    private AlarmManager am;
    private DBManager dbManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        if (dbManager == null) {
            dbManager = new DBManager(ScheduleActivity.this, "AlarmCall", null, 1);
            dbManager.ReadDB();
        }

        Button button = (Button)findViewById(R.id.makeschedule);
        button.setOnClickListener(this);

        // text뷰가 아니라 리스트 뷰로 뜨도록
        TextView textView = (TextView)findViewById(R.id.schedulelist);
        ArrayList<Schedule> arrayList = dbManager.getSchedule();
        if(arrayList.size()>0)
            textView.setText(""+arrayList.get(0).getName().toString()+", "+arrayList.get(0).getModename());
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        intent = new Intent(ScheduleActivity.this, ScheduleSetActivity.class);
        startActivity(intent);

    }

    private void setAlarm(Calendar targetCal, String string) {

        Context context = getApplicationContext();

        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("set",string);

        PendingIntent amIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        am.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), amIntent);

        Toast.makeText(context, "Reset time is " + targetCal.getTime(), Toast.LENGTH_SHORT).show();
    }


    private void resetAlarm(Calendar targetCal,View v) {
        Log.i(Tag, "reset Alarm start");
        Context context = getApplicationContext();
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        // intent.putExtra("ringer mode",org_ring);
        PendingIntent amIntent2 = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        // AlarmManager???뚮엺 ?쒓컙 ?ㅼ젙
        am.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), amIntent2);
        Toast.makeText(context, "Reset time is " + targetCal.getTime(), Toast.LENGTH_SHORT).show();
    }
}
