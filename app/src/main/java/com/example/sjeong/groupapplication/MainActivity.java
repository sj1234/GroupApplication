package com.example.sjeong.groupapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

int a ,z=0;
    Button bttn_option;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bttn_option = (Button) findViewById(R.id.bttn_option);

        bttn_option.setOnClickListener(new View.OnClickListener() {



            public void onClick(View v) {

                Intent intent1 = new Intent(MainActivity.this, OptionActivity.class);

                startActivity(intent1);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();


            }

        });


    }



}






