package com.example.sjeong.groupapplication;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



public class OptionActivity extends AppCompatActivity implements OnClickListener{

    final Context context = this;
    Button bttn_main, bttn_exm,bttn_mode ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        bttn_main = (Button) findViewById(R.id.bttn_main);
        bttn_exm = (Button) findViewById(R.id.bttn_exm);
        bttn_mode = (Button) findViewById(R.id.bttn_mode);

        bttn_main.setOnClickListener(this);
        bttn_exm.setOnClickListener(this);
        bttn_mode.setOnClickListener(this);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bttn_main:
                Intent intent1 = new Intent(OptionActivity.this,MainActivity.class);

                startActivity(intent1);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
                break;

            case R.id.bttn_mode:

                Intent intent4 = new Intent(OptionActivity.this,ModeActivity.class);

                startActivity(intent4);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();


            default:
                break;
        }
    }
}




