package com.example.sjeong.groupapplication;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;



public class ScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    private String Tag = "test ScheduleActivity";
    private AlarmManager am;
    private DBManager dbManager;
    private ListView list;
    ScheduleListAdapter adapter;
    Boolean setRec = Boolean.FALSE;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(Tag, "on Create ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        if (dbManager == null) {
            dbManager = new DBManager(ScheduleActivity.this, "AlarmCall", null, 1);
            dbManager.ReadDB();
        }

        Button make = (Button)findViewById(R.id.makeschedule);
        ImageButton home = (ImageButton) findViewById(R.id.home_schedule);
        ImageButton mode = (ImageButton) findViewById(R.id.mode_schedule);
        ImageButton schedule= (ImageButton) findViewById(R.id.schedule_schedule);
        ImageButton setting = (ImageButton) findViewById(R.id.setting_schedule);

        make.setOnClickListener(this);
        home.setOnClickListener(this);
        mode.setOnClickListener(this);
        schedule.setOnClickListener(this);
        setting.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<Schedule> arrayList = dbManager.getSchedules();
        adapter = new ScheduleListAdapter(this, R.layout.listview2, arrayList, onClickListener);
        list = (ListView) findViewById(R.id.schedulelistview);
        list.setAdapter(adapter);

    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String position = v.getTag().toString();

            Intent intent;
            switch (v.getId()) {
                case R.id.scheduletime:
                    Log.i(Tag, "scheduleview "+position );
                    intent = new Intent(ScheduleActivity.this, ScheduleSetActivity.class);
                    intent.putExtra("Position", position);
                    startActivity(intent);
                    break;
                case R.id.modename:
                    Log.i(Tag, "scheduleview "+position);
                    intent = new Intent(ScheduleActivity.this, ScheduleSetActivity.class);
                    intent.putExtra("Position", position);
                    startActivity(intent);
                    break;
                case R.id.scheduleon:
                    Log.i(Tag, "scheduleview "+position);
                    Schedule schedule = dbManager.getSchedule(Integer.parseInt(position));
                    String starttime = schedule.getStart().toString();
                    String endtime = schedule.getEnd().toString();
                    String chgMode = schedule.getModename();
                    String orgMode;
                    //sunday = 1이라서 0의 자리에 0
                    int[] week = { 0, schedule.getSun(), schedule.getMon(),schedule.getTue(),schedule.getWed(),schedule.getThu(),schedule.getFri(),schedule.getSat() }; // sunday=1 이라서 0의 자리에는 아무 값이나 넣었음
                    int i = schedule.getId();
                    Log.i(Tag, "on schedule Id is "+i);

                    // Premode
                    SharedPreferences preferences = getSharedPreferences("Mode", Activity.MODE_PRIVATE);
                    if(preferences.getString("set", "off").equals("off")) {
                        orgMode = null;
                        schedule.setPremodename(orgMode);
                        dbManager.updateSchedule(schedule);
                    }
                    else {
                        orgMode =  preferences.getString("name", "null");
                        schedule.setPremodename(orgMode);
                        dbManager.updateSchedule(schedule);
                    }

                    String[] start = starttime.split(":");
                    String[] end = endtime.split(":");

                    Log.i(Tag, "schedule on "+start[0]+"시 "+start[1]+"분 부터 "+end[0]+"시 "+end[1]+"분 까지");
                    // start[0] : 시작시간의 시간  / start[1] : 시작시간의 분 / end[0] : 종료시간의 시간 / end[1] : 종료시간의 분
                    // 여기에서 알람설정함수 부르면 될것같아

                    Calendar calStart = Calendar.getInstance();
                    Calendar calEnd = Calendar.getInstance();
                    int starttimeHOUR = Integer.parseInt(start[0]);
                    int starttimeMINUTE = Integer.parseInt(start[1]);
                    int endtimeHOUR = Integer.parseInt(end[0]);
                    int endtimeMINUTE = Integer.parseInt(end[1]);
                    int startReq=2*i;
                    int endReq=(2*i)+1;
                    calStart.set(calStart.get(Calendar.YEAR), calStart.get(Calendar.MONTH) , calStart.get(Calendar.DATE), starttimeHOUR, starttimeMINUTE,0);
                    Log.i(Tag, " onbutton setting 시간 "+starttimeHOUR+"  setting 분" +starttimeMINUTE);
                    Log.i(Tag, " 스케줄 시작 시간 :  "+calStart.getTimeInMillis());
                    calEnd.set(calEnd.get(Calendar.YEAR), calEnd.get(Calendar.MONTH) , calEnd.get(Calendar.DATE), endtimeHOUR, endtimeMINUTE,0);
                    Log.i(Tag, " onbutton setting 시간 :  "+endtimeHOUR+"  setting 분 : " +endtimeMINUTE);
                    Log.i(Tag, " 스케줄 종료 시간 :  "+calEnd.getTimeInMillis());
                    amStartSet(calStart,v,chgMode,startReq,week,i-1);
                    amEndSet(calEnd,v,orgMode,endReq,week,i-1);
                    break;

                case R.id.scheduleoff:
                    Log.i(Tag, "schedule off "+position);
                    // 종료하는건 아직 안했어
                    Schedule scheduleoff = dbManager.getSchedule(Integer.parseInt(position));
                    String starttimeoff = scheduleoff.getStart().toString();
                    String endtimeoff = scheduleoff.getEnd().toString();
                    //sunday = 1이라서 0의 자리에 false.
                    int[] weekoff = { 0, scheduleoff.getSun(), scheduleoff.getMon(),scheduleoff.getTue(),scheduleoff.getWed(),scheduleoff.getThu(),scheduleoff.getFri(),scheduleoff.getSat() }; // sunday=1 이라서 0의 자리에는 아무 값이나 넣었음
                    int ioff = scheduleoff.getId();
                    Log.i(Tag, "off schedule Id is "+ioff);

                    String[] startoff = starttimeoff.split(":");
                    String[] endoff = endtimeoff.split(":");

                    Log.i(Tag, "schedule on "+startoff[0]+"시 "+startoff[1]+"분 부터 "+endoff[0]+"시 "+endoff[1]+"분 까지");
                    // start[0] : 시작시간의 시간  / start[1] : 시작시간의 분 / end[0] : 종료시간의 시간 / end[1] : 종료시간의 분
                    // 여기에서 알람설정함수 부르면 될것같아

                    Calendar calStartoff = Calendar.getInstance();
                    Calendar calEndoff = Calendar.getInstance();
                    int starttimeHOURoff = Integer.parseInt(startoff[0]);
                    int starttimeMINUTEoff = Integer.parseInt(startoff[1]);
                    int endtimeHOURoff = Integer.parseInt(endoff[0]);
                    int endtimeMINUTEoff = Integer.parseInt(endoff[1]);
                    calStartoff.set(calStartoff.get(Calendar.YEAR), calStartoff.get(Calendar.MONTH) , calStartoff.get(Calendar.DATE), starttimeHOURoff, starttimeMINUTEoff,0);
                    Log.i(Tag, " onbutton setting 시간 "+starttimeHOURoff+"  setting 분" +starttimeMINUTEoff);
                    Log.i(Tag, " 스케줄 시작 시간 :  "+calStartoff.getTime());
                    calEndoff.set(calEndoff.get(Calendar.YEAR), calEndoff.get(Calendar.MONTH) , calEndoff.get(Calendar.DATE), endtimeHOURoff, endtimeMINUTEoff,0);
                    Log.i(Tag, " onbutton setting 시간 :  "+endtimeHOURoff+"  setting 분 : " +endtimeMINUTEoff);
                    Log.i(Tag, " 스케줄 종료 시간 :  "+calEndoff.getTime());
                    amstartClear(calStartoff,v,2*ioff);
                    amendClear(calEndoff,v,(2*ioff)+1);
                    break;
                default:
                    break;

            }
        }
    };

    @Override
    public void onClick(View v) {

        Intent intent = null;

        switch (v.getId()) {
            case R.id.makeschedule:
                if(dbManager.getModesName().size()==0)
                    Toast.makeText(ScheduleActivity.this, "모드를 생성하십시오", Toast.LENGTH_SHORT).show();
                else {
                    intent = new Intent(ScheduleActivity.this, ScheduleSetActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.home_schedule:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.mode_schedule:
                intent = new Intent(this, ModeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.schedule_schedule:
                break;
            case R.id.setting_schedule:
                intent = new Intent(this, PrefSettingActivity.class);
                startActivity(intent);
                finish();
                break;
            default :
                break;
        }
    }

    // 알람설정함수
    public void amStartSet(Calendar calStart, View v,String chgMode,int i,int[] week, int pos){ // 시작 스케줄 알람
        Log.i(Tag,"set Alarm Start");
        Context context = getApplicationContext();
        am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        // 시작시간
        setRec=Boolean.TRUE;
        intent.putExtra("alReceiver",setRec);
        intent.putExtra("change mode",chgMode);
        intent.putExtra("weekday",week);
        intent.putExtra("position",pos); // pos = 0부터, id= 1부터
        intent.putExtra("reqid",i);
        Log.i(Tag, "alarmStart request code is "+i);
        PendingIntent amIntent = PendingIntent.getBroadcast(context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setExact(AlarmManager.RTC_WAKEUP, calStart.getTimeInMillis(),amIntent);
        Log.i(Tag, " 스케줄 시작 시간 :  "+calStart.getTimeInMillis());
    }
    public void amEndSet(Calendar calEnd, View v, String orgMode, int i,int[] week, int pos){ // 종료 스케줄 알람
        Log.i(Tag,"set Alarm End");
        Context context = getApplicationContext();
        am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent2 = new Intent(context, AlarmReceiver.class);
        //종료시간
        setRec=Boolean.FALSE;
        intent2.putExtra("alReceiver",setRec);
        intent2.putExtra("original mode",orgMode);
        intent2.putExtra("weekday",week);
        intent2.putExtra("position",pos); // pos = 0부터, id= 1부터
        intent2.putExtra("reqid",i);
        Log.i(Tag, "alarmEnd request code is "+i);
        PendingIntent amIntent2 = PendingIntent.getBroadcast(context, i, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setExact(AlarmManager.RTC_WAKEUP, calEnd.getTimeInMillis(), amIntent2);
        Log.i(Tag, " 스케줄 종료 시간 :  "+calEnd.getTimeInMillis());
    }

    public void amstartClear(Calendar calStartoff, View v,int ioff){
       /*
        if(amIntent == null){
            Log.i(Tag, "There is no alarm");
            Toast.makeText(getApplicationContext(), "There is no alarm " , Toast.LENGTH_SHORT).show();
        }
        */
        //else {
            Context context = getApplicationContext();
            Intent intent = new Intent(context,AlarmReceiver.class);
            setRec=Boolean.TRUE;
            intent.putExtra("alReceiver",setRec);
            Log.i(Tag, "clearStart request code is "+ioff);
            am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent amIntent = PendingIntent.getBroadcast(context,ioff,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            am.cancel(amIntent);
            Log.i(Tag, "Clear Alarm"); //}
        }

        public void amendClear(Calendar calendoff,View v,int ioff){
            Context context = getApplicationContext();
            Intent intent = new Intent(context,AlarmReceiver.class);
            setRec=Boolean.FALSE;
            intent.putExtra("alReceiver",setRec);
            Log.i(Tag, "clearEnd request code is "+ioff);
            am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent amIntent = PendingIntent.getBroadcast(context,ioff,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            am.cancel(amIntent);
            Log.i(Tag, "Clear Alarm");
        }
}
