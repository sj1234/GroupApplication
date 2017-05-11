package com.example.sjeong.groupapplication;

import android.app.AlarmManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;



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
                    Schedule schedule = dbManager.getSchedule(Integer.parseInt(position));
                    String starttime = schedule.getStart().toString();
                    String endtime = schedule.getEnd().toString();

                    String[] start = starttime.split(":");
                    String[] end = endtime.split(":");

                    Log.i(Tag, "schedule on "+start[0]+"시 "+start[1]+"분 부터 "+end[0]+"시 "+end[1]+"분 까지");
                    // start[0] : 시작시간의 시간  / start[1] : 시작시간의 분 / end[0] : 종료시간의 시간 / end[1] : 종료시간의 분
                    // 여기에서 알람설정함수 부르면 될것같아
                    break;
                case R.id.scheduleoff:
                    Log.i(Tag, "schedule off "+position);
                    // 종료하는건 아직 안했어
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
                intent = new Intent(ScheduleActivity.this, ScheduleSetActivity.class);
                startActivity(intent);
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

    // 알람설정함수는 여기다가 만들어
    //publid void 알람설정set(){}
}
