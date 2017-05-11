package com.example.sjeong.groupapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton home;
    private ImageButton mode;
    private ImageButton schedule;
    private ImageButton setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        home = (ImageButton) findViewById(R.id.home);
        mode = (ImageButton) findViewById(R.id.mode);
        schedule= (ImageButton) findViewById(R.id.schedule);
        setting = (ImageButton) findViewById(R.id.setting);

        home.setOnClickListener(this);
        mode.setOnClickListener(this);
        schedule.setOnClickListener(this);
        setting.setOnClickListener(this);


      /* SharedPreferences preferences = getSharedPreferences("Later", Activity.MODE_PRIVATE);
        Switch switchlater = (Switch) findViewById(R.id.later);
        switchlater.setChecked(preferences.getString("onoff", "off").equals("on"));
        switchlater.setText("나중에 알림");

        switchlater.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences preferences = getSharedPreferences("Later", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                if (isChecked == true){
                    editor.putString("onoff", "on");
                } else {
                    editor.putString("onoff", "off");
                }
                editor.commit();
            }
        });*/
    }



    @Override
    protected void onResume(){
        super.onResume();
        TextView nowname = (TextView)findViewById(R.id.nowname);
        TextView nowstar = (TextView)findViewById(R.id.nowstar);
        TextView nowcontact = (TextView)findViewById(R.id.nowcontact);
        TextView nowunknown = (TextView)findViewById(R.id.nowunknown);
        TextView nowtimecount = (TextView)findViewById(R.id.nowtimecount);
        NowMode(nowname, nowstar,nowcontact,nowunknown,nowtimecount);
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;

        switch (v.getId()) {
            case R.id.home:
                break;
            case R.id.mode:
                intent = new Intent(MainActivity.this, ModeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.schedule:
                intent = new Intent(MainActivity.this, ScheduleActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.setting:
                intent = new Intent(MainActivity.this, PrefSettingActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    public void NowMode(TextView nowname, TextView nowstar,TextView nowcontact,TextView nowunknown,TextView nowtimecount){

        String name;
        int star, contact, unknown, time, count;

        // Preferences 생성
        SharedPreferences preferences = getSharedPreferences("Mode", Activity.MODE_PRIVATE);

        if(preferences.getString("set", "off").equals("off")) {
            nowname.setText("현재 모드 : off");
            nowstar.setText("");
            nowcontact.setText("");
            nowunknown.setText("");
            nowtimecount.setText("");
            return ;
        }
        else{
            // 현재 상태 띄우기
            name = preferences.getString("name", "null");
            star = preferences.getInt("star", 4);
            contact = preferences.getInt("contact", 4);
            unknown = preferences.getInt("unknown", 4);
            time = preferences.getInt("time", 0);
            count = preferences.getInt("count", 0);

            nowname.setText("현재 모드 : "+name);
            nowstar.setText("즐겨찾기 : "+RingInformation(star));
            nowcontact.setText("즐겨찾기 외 저장된 번호 : "+RingInformation(contact));
            nowunknown.setText("모르는 번호 : "+RingInformation(unknown));
            nowtimecount.setText("긴급전화 : "+time+"분안에 "+count+"회 이상");
        }
    }

    public String RingInformation(int i){
        switch(i){
            case 1:
                return "벨소리";
            case 2:
                return "진동";
            case 3:
                return "무음";
            case 4:
                return "차단";
        }
        return null;
    }



}







