package com.example.sjeong.groupapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;


public class OptionActivity extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);


        SharedPreferences preferences = getSharedPreferences("Later", Activity.MODE_PRIVATE);
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
        });

        Button home = (Button) findViewById(R.id.home_option);
        Button mode = (Button) findViewById(R.id.mode_option);
        Button schedule= (Button) findViewById(R.id.schedule_option);
        Button setting = (Button) findViewById(R.id.setting_option);

        home.setOnClickListener(this);
        mode.setOnClickListener(this);
        schedule.setOnClickListener(this);
        setting.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent=null;

        switch (v.getId()) {
            case R.id.home_option:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.mode_option:
                intent = new Intent(this, ModeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.schedule_option:
                intent = new Intent(this, ScheduleActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.setting_option:
                break;
            default:
                break;
        }
    }
}




