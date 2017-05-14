package com.example.sjeong.groupapplication;

import android.app.Activity;
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

public class ModeActivity extends AppCompatActivity implements View.OnClickListener {

    private String Tag = "test ModeActive";
    private DBManager dbManager;
    private Context context;
    private ListAdapter listAdapter;
    private ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        context = this;

        // DB생성
        if (dbManager == null) {
            dbManager = new DBManager(ModeActivity.this, "AlarmCall", null, 1);
            dbManager.ReadDB();
        }

        Button makemode = (Button)findViewById(R.id.makemode);
        Button nomode = (Button)findViewById(R.id.nomode);
        ImageButton home = (ImageButton) findViewById(R.id.home_mode);
        ImageButton mode = (ImageButton) findViewById(R.id.mode_mode);
        ImageButton schedule= (ImageButton) findViewById(R.id.schedule_mode);
        ImageButton setting = (ImageButton) findViewById(R.id.setting_mode);

        makemode.setOnClickListener((View.OnClickListener) this);
        nomode.setOnClickListener((View.OnClickListener) this);
        home.setOnClickListener(this);
        mode.setOnClickListener(this);
        schedule.setOnClickListener(this);
        setting.setOnClickListener(this);

    }

    @Override
    protected void onResume(){
        super.onResume();
        Toast.makeText(this, "OnResume 호출", Toast.LENGTH_SHORT).show();
        ArrayList<String> modes = dbManager.getModesName();
        listAdapter = new ListAdapter(this, R.layout.listview, modes, onClickListener);
        list = (ListView) findViewById(R.id.listView);
        list.setAdapter(listAdapter);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.makemode:
                intent = new Intent(this, ModeSetActivity.class);
                startActivity(intent);
                break;
            case R.id.nomode:
                SharedPreferences preferences =context.getSharedPreferences("Mode", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("set", "off");
                editor.commit();

                // 위젯 변경
                Intent wintent = new Intent(context, AppWidget.class);
                wintent.setAction("com.example.sjeong.groupapplication.CHANGE");
                PendingIntent pendindintent = PendingIntent.getBroadcast(context, 0, wintent, 0);
                try {
                    pendindintent.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.home_mode:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.mode_mode:
                break;
            case R.id.schedule_mode:
                intent = new Intent(this, ScheduleActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.setting_mode:
                intent = new Intent(this, PrefSettingActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String modename = v.getTag().toString();

            switch (v.getId()) {
                case R.id.itemmode:
                    Log.i("test tag", modename);
                    Intent intent = new Intent(ModeActivity.this, ModeSetActivity.class);
                    intent.putExtra("Name",modename);
                    startActivity(intent);
                    break;
                case R.id.select:
                    Log.i("test tag", modename);
                    SetNowMode(modename);

                    // 위젯 변경
                    Intent wintent = new Intent(context, AppWidget.class);
                    wintent.setAction("com.example.sjeong.groupapplication.CHANGE");
                    PendingIntent pendindintent = PendingIntent.getBroadcast(context, 0, wintent, 0);
                    try {
                        pendindintent.send();
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }

                    break;
                case R.id.delete:
                    Log.i("test tag", modename);
                    SharedPreferences preferences= context.getSharedPreferences("Mode", Activity.MODE_PRIVATE);
                    if(preferences.getString("name", "null").equals(modename)) {
                        if(preferences.getString("set", "off").equals("off")){
                            dbManager.deleteMode(modename);

                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("name", "null");
                            editor.commit();

                            Toast.makeText(context, "삭제 완료", Toast.LENGTH_SHORT).show();
                            onResume();
                        }
                        else
                            Toast.makeText(context, "현재 모드라 삭제 불가능 합니다.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        dbManager.deleteMode(modename);
                        Toast.makeText(context, "삭제 완료", Toast.LENGTH_SHORT).show();
                        onResume();
                    }
                    break;

            }
        }
    };

    public void SetNowMode(String name){

        Mode mode = new Mode();
        mode = dbManager.getMode(name);

        SharedPreferences preferences = getSharedPreferences("Mode", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("set", "on");
        editor.putString("name", mode.getName());
        editor.putInt("star", mode.getStar());
        editor.putInt("contact", mode.getContact());
        editor.putInt("unknown", mode.getUnknown());
        editor.putInt("time", mode.getTime());
        editor.putInt("count", mode.getCount());

        editor.commit();

        Log.i(Tag, "Set Now Mode");
    }

}