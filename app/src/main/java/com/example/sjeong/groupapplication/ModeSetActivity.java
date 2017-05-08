package com.example.sjeong.groupapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ModeSetActivity extends AppCompatActivity implements View.OnClickListener{


    private Context context;
    private String Tag="test ModeSetActive";
    private DBManager dbManager;
    private Mode mode;
    private String name;

    private TextView startxt,contacttxt,unknowntxt,timetxt,counttxt;
    private EditText modename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_set);
        context = this;

        // DB생성
        if(dbManager==null) {
            dbManager = new DBManager(ModeSetActivity.this, "AlarmCall", null, 1);
            dbManager.ReadDB();
        }

        Intent intent = getIntent();
        name = intent.getStringExtra("Name");

        Button star = (Button) this.findViewById(R.id.star);
        star.setOnClickListener((View.OnClickListener) this);

        Button contact = (Button) this.findViewById(R.id.contact);
        contact.setOnClickListener((View.OnClickListener) this);

        Button unknown = (Button) this.findViewById(R.id.unknown);
        unknown.setOnClickListener((View.OnClickListener) this);

        Button set = (Button) this.findViewById(R.id.set);
        set.setOnClickListener((View.OnClickListener) this);

        modename = (EditText) findViewById(R.id.modename);
        startxt = (TextView) findViewById(R.id.startxt);
        contacttxt = (TextView) findViewById(R.id.contacttxt);
        unknowntxt = (TextView) findViewById(R.id.unknowntxt);
        timetxt = (TextView) findViewById(R.id.timetxt);
        counttxt = (TextView) findViewById(R.id.counttxt);

        mode = new Mode();

        if(name != null) {
            mode=dbManager.getMode(name);
            modename.setText(name);
            startxt.setText( RingInformation(mode.getStar()));
            contacttxt.setText(RingInformation(mode.getContact()));
            unknowntxt.setText(RingInformation(mode.getUnknown()));
        }
        else {
            mode = new Mode();
            modename.setText("모드 이름");
            startxt.setText("수신음을 선택해 주세요");
            contacttxt.setText("수신음을 선택해 주세요");
            unknowntxt.setText("수신음을 선택해 주세요");
        }
        Spinner s1 = (Spinner)findViewById(R.id.timespinner);
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                timetxt.setText("TIME : "+parent.getItemAtPosition(position));
                mode.setTime(Integer.parseInt(""+parent.getItemAtPosition(position)));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        Spinner s2 = (Spinner)findViewById(R.id.countspinner);
        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                counttxt.setText("COUNT : "+parent.getItemAtPosition(position));
                mode.setCount(Integer.parseInt(""+parent.getItemAtPosition(position)));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });




    }

    @Override
    public void onClick(View v) {

        SharedPreferences preferences =context.getSharedPreferences("Mode", Activity.MODE_PRIVATE);

        switch (v.getId()) {
            case R.id.star:
                final CharSequence[] items1 = {"벨소리", "진동", "무음", "차단"};
                AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(context);

                // 제목셋팅
                alertDialogBuilder1.setTitle("모드 선택 목록");
                alertDialogBuilder1.setSingleChoiceItems(items1, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                startxt.setText(items1[id]);
                                if(items1[id].equals("벨소리"))
                                    mode.setStar(1);
                                else if(items1[id].equals("진동"))
                                    mode.setStar(2);
                                else if(items1[id].equals("무음"))
                                    mode.setStar(3);
                                else if(items1[id].equals("차단"))
                                    mode.setStar(4);

                                // 프로그램을 종료한다
                                Toast.makeText(getApplicationContext(), items1[id] + " 선택했습니다.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                // 다이얼로그 생성
                AlertDialog alertDialog1 = alertDialogBuilder1.create();
                // 다이얼로그 보여주기
                alertDialog1.show();
                break;
            case R.id.contact:
                final CharSequence[] items2 = {"벨소리", "진동", "무음", "차단"};
                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(context);

                // 제목셋팅
                alertDialogBuilder2.setTitle("모드 선택 목록");
                alertDialogBuilder2.setSingleChoiceItems(items2, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                contacttxt.setText(items2[id]);
                                if(items2[id].equals("벨소리"))
                                    mode.setContact(1);
                                else if(items2[id].equals("진동"))
                                    mode.setContact(2);
                                else if(items2[id].equals("무음"))
                                    mode.setContact(3);
                                else if(items2[id].equals("차단"))
                                    mode.setContact(4);

                                // 프로그램을 종료한다
                                Toast.makeText(getApplicationContext(), items2[id] + " 선택했습니다.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                // 다이얼로그 생성
                AlertDialog alertDialog2 = alertDialogBuilder2.create();
                // 다이얼로그 보여주기
                alertDialog2.show();
                break;
            case R.id.unknown:
                final CharSequence[] items3 = {"벨소리", "진동", "무음", "차단"};
                AlertDialog.Builder alertDialogBuilder3 = new AlertDialog.Builder(context);

                // 제목셋팅
                alertDialogBuilder3.setTitle("모드 선택 목록");
                alertDialogBuilder3.setSingleChoiceItems(items3, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                unknowntxt.setText(items3[id]);
                                if(items3[id].equals("벨소리"))
                                    mode.setUnknown(1);
                                else if(items3[id].equals("진동"))
                                    mode.setUnknown(2);
                                else if(items3[id].equals("무음"))
                                    mode.setUnknown(3);
                                else if(items3[id].equals("차단"))
                                    mode.setUnknown(4);

                                // 프로그램을 종료한다
                                Toast.makeText(getApplicationContext(), items3[id] + " 선택했습니다.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                // 다이얼로그 생성
                AlertDialog alertDialog3 = alertDialogBuilder3.create();
                // 다이얼로그 보여주기
                alertDialog3.show();
                break;
            case R.id.set:
                if(name==null) {
                    mode.setName(modename.getText().toString());
                    dbManager.insertMode(mode);
                }
                else if(name.equals(preferences.getString("name", "null")))
                {
                    mode.setName(modename.getText().toString());
                    dbManager.updateMode(name, mode);

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("set", "on");
                    editor.putString("name", mode.getName());
                    editor.putInt("star", mode.getStar());
                    editor.putInt("contact", mode.getContact());
                    editor.putInt("unknown", mode.getUnknown());
                    editor.putInt("time", mode.getTime());
                    editor.putInt("count", mode.getCount());
                    editor.commit();

                    Log.i(Tag, "Update Now Mode");
                }
                else {
                    mode.setName(modename.getText().toString());
                    dbManager.updateMode(name, mode);
                }
                ModeSetActivity.this.finish();
                break;
            default:
                break;
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
