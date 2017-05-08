package com.example.sjeong.groupapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

int a ,z=0;
    Button bttn_option;
    Button example;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView nowname = (TextView)findViewById(R.id.nowname);
        TextView nowstar = (TextView)findViewById(R.id.nowstar);
        TextView nowcontact = (TextView)findViewById(R.id.nowcontact);
        TextView nowunknown = (TextView)findViewById(R.id.nowunknown);
        TextView nowtimecount = (TextView)findViewById(R.id.nowtimecount);
        NowMode(nowname, nowstar,nowcontact,nowunknown,nowtimecount);


        bttn_option = (Button) findViewById(R.id.bttn_option);
        example = (Button) findViewById(R.id.example);

        bttn_option.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent1 = new Intent(MainActivity.this, OptionActivity.class);

                startActivity(intent1);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
            }

        });

        example.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, ExampleActivity.class);

                startActivity(intent2);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();

            }
        });
    }

    public void NowMode(TextView nowname, TextView nowstar,TextView nowcontact,TextView nowunknown,TextView nowtimecount){

        String name;
        int star, contact, unknown, time, count;

        // Preferences 생성
        SharedPreferences preferences = getSharedPreferences("Mode", Activity.MODE_PRIVATE);

        if(preferences.getString("set", "off").equals("off")) {
            nowname.setText("현재 모드 : off");
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






