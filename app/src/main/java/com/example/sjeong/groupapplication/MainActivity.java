package com.example.sjeong.groupapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

int a ,z=0;
    Button bttn_option;
    Button example;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

        // Preferences 생성
        SharedPreferences pref = getSharedPreferences("Mode", Activity.MODE_PRIVATE);
        // 현재 상태 띄우기


    }



}






