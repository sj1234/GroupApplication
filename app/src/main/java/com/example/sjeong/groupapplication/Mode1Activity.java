package com.example.sjeong.groupapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.view.View.OnClickListener;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.view.View.*;

public class Mode1Activity extends AppCompatActivity  implements OnClickListener {
    int z =0;
    String databaseName = "db";
    String tableName = "mode";
    TextView status;
    boolean databaseCreated = false;
    boolean tableCreated = false;
    private static int DATABASE_VERSION = 1;
    private static String TABLE_NAME;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Button bttn_favor, bttn_etc, bttn_unkn, bttn_emgc,bttn_back1;
    final Context context = this;
    String[] items = {"mode1","mode2","mode3","mode4"};
    int a;
    String value;
    String b;
    TextView textView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mode_1);
        textView1 = (TextView) findViewById(R.id.textView1);

        createDatabase(databaseName);
        createTable(tableName);
        int count = insertRecord(tableName);
        boolean isOpen = openDatabase();
        if(isOpen) {
            executeRawQuery();
            executeRawQueryParam();
        }

        bttn_favor = (Button) findViewById(R.id.bttn_favor);
        bttn_etc = (Button) findViewById(R.id.bttn_etc);
        bttn_unkn = (Button) findViewById(R.id.bttn_unkn);
        bttn_emgc = (Button) findViewById(R.id.bttn_emgc);
        bttn_back1 = (Button) findViewById(R.id.bttn_back1);


        bttn_favor.setOnClickListener(this);
        bttn_etc.setOnClickListener(this);
        bttn_unkn.setOnClickListener(this);
        bttn_emgc.setOnClickListener(this);
        bttn_back1.setOnClickListener(this);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_dropdown_item,items);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            // 아이템이 선택되었을 때 호출됨
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //textView.setText(items[position]);
                a=position;

            }

            // 아무것도 선택되지 않았을 때 호출됨
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // textView.setText("");
            }
        });

        // textView1.setText(""+value);



    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bttn_favor:
                b="favorites";
                final CharSequence[] items1 = {"벨소리", "진동", "무음", "차단"};
                AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(
                        context);

                // 제목셋팅
                alertDialogBuilder1.setTitle("모드 선택 목록");
                alertDialogBuilder1.setSingleChoiceItems(items1, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                // 프로그램을 종료한다

                                Toast.makeText(getApplicationContext(),
                                        items1[id] + " 선택했습니다.",
                                        Toast.LENGTH_SHORT).show();
                                updateRecord(tableName, a, b, id);


                                dialog.dismiss();
                            }
                        });

                // 다이얼로그 생성
                AlertDialog alertDialog1 = alertDialogBuilder1.create();

                // 다이얼로그 보여주기
                alertDialog1.show();

                break;

            case R.id.bttn_etc:

                final CharSequence[] items2 = {"벨소리", "진동", "무음", "차단"};
                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(
                        context);

                // 제목셋팅
                alertDialogBuilder2.setTitle("모드 선택 목록");
                alertDialogBuilder2.setSingleChoiceItems(items2, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                // 프로그램을 종료한다
                                Toast.makeText(getApplicationContext(),
                                        items2[id] + " 선택했습니다.",
                                        Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                // 다이얼로그 생성
                AlertDialog alertDialog2 = alertDialogBuilder2.create();

                // 다이얼로그 보여주기
                alertDialog2.show();
                break;

            case R.id.bttn_unkn:

                final CharSequence[] items3 = {"벨소리", "진동", "무음", "차단"};
                AlertDialog.Builder alertDialogBuilder3 = new AlertDialog.Builder(
                        context);

                // 제목셋팅
                alertDialogBuilder3.setTitle("모드 선택 목록");
                alertDialogBuilder3.setSingleChoiceItems(items3, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                // 프로그램을 종료한다
                                Toast.makeText(getApplicationContext(),
                                        items3[id] + " 선택했습니다.",
                                        Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                // 다이얼로그 생성
                AlertDialog alertDialog3 = alertDialogBuilder3.create();

                // 다이얼로그 보여주기
                alertDialog3.show();
                break;

            case R.id.bttn_emgc:

                final CharSequence[] items4 = {"벨소리", "진동", "무음", "차단"};
                AlertDialog.Builder alertDialogBuilder4 = new AlertDialog.Builder(
                        context);

                // 제목셋팅
                alertDialogBuilder4.setTitle("모드 선택 목록");
                alertDialogBuilder4.setSingleChoiceItems(items4, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                // 프로그램을 종료한다
                                Toast.makeText(getApplicationContext(),
                                        items4[id] + " 선택했습니다.",
                                        Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                // 다이얼로그 생성
                AlertDialog alertDialog4 = alertDialogBuilder4.create();

                // 다이얼로그 보여주기
                alertDialog4.show();
                break;

            case R.id.bttn_back1:

                Intent intent9 = new Intent(Mode1Activity.this,OptionActivity.class);
                startActivity(intent9);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
                break;


        }
    }


    public void createDatabase(String name) {

        try {
            db = openOrCreateDatabase(
                    name,
                    Activity.MODE_PRIVATE,
                    null);

            databaseCreated = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void createTable(String name) {

        db.execSQL("create table if not exists " + name + "("
                + " _id integer PRIMARY KEY autoincrement, "
                + " modename text, "
                + " emertime text, "
                + " emercount integer, "
                + " favorites integer, "
                + " nonfavorites integer, "
                + " unknown integer);" );
        tableCreated = true;
    }

    public int insertRecord(String name) {

        int count = 1;
        db.execSQL( "insert into " + name + "(modename, emertime, emercount, favorites, nonfavorites, unknown) values ('Ring', '2007:05:03:12:36', 2, 1, 2, 1);" );

        return count;
    }

    public  int updateRecord(String name, int mode, String column, int id)  {
        int count =1;
        db.execSQL("update " + name + " SET "+column+" = "+ id + " where modename = " + mode);

        return count;
    }

    public void executeRawQuery() {

        Cursor c1 = db.rawQuery("select favorites from mode" , null);
//        println("cursor count : " + c1.getCount());
        //      value = c1.getString(c1.getColumnIndex("favorites"));
        while(c1.moveToNext()) {
            textView1.append(c1.getString(3));
            //value = c1.getString(c1.getColumnIndex("favorites"));
        }
//        println("record count : " + c1.getInt(0));

        c1.close();

    }
    public void executeRawQueryParam() {

        String SQL = "select modename, emertime, emercount, favorites, nonfavorites, unknown "
                + " from " +tableName;
        String[] args= {};

        Cursor c1 = db.rawQuery(SQL, args);
        int recordCount = c1.getCount();
        int z;

        for (int i = 0; i < recordCount; i++) {
            c1.moveToNext();
            String name = c1.getString(0);
            int age = c1.getInt(1);
            String phone = c1.getString(2);


        }

        c1.close();
    }

    public void executeRawQueryParam2() {

        String[] columns = {"modename", "emertime", "emercount", "favorites", "nonfavorites", "unknown"};
        String whereStr = ""; // 질문
        String[] whereParams = {};
        Cursor c1 = db.query(TABLE_NAME, columns,
                whereStr, whereParams,
                null, null, null);

        int recordCount = c1.getCount();
//        println("cursor count : " + recordCount + "\n");

        for (int i = 0; i < recordCount; i++) {
            c1.moveToNext();
            String name = c1.getString(0);
            int age = c1.getInt(1);
            String phone = c1.getString(2);

        }

        c1.close();

    }

    public boolean openDatabase() {
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        return true;
    }

    public class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, "MODE", null, DATABASE_VERSION);
        }


        public void onCreate(SQLiteDatabase db) {
//        println("creating table [" + TABLE_NAME + "].");

            try {
                String DROP_SQL = "drop table if exists " + tableName;
                db.execSQL(DROP_SQL);
            } catch (Exception ex) {
//                Log.e(TAG, "Exception in DROP_SQL", ex);
            }

            String CREATE_SQL = "create table " + tableName + "("
                    + " _id integer PRIMARY KEY autoincrement, "
                    + " modename text, "
                    + " emertime text, "
                    + " emercount integer, "
                    + " favorites integer, "
                    + " nonfavorites integer, "
                    + " unknown integer);" ;

            try {
                db.execSQL(CREATE_SQL);
            } catch (Exception ex) {
//                Log.e(TAG, "Exception in CREATE_SQL", ex);
            }

//            println("inserting records.");

            try {
//                db.execSQL("insert into " + tableName + "(name, age, phone) values ('John', 20, '010-7788-1234');");
            } catch (Exception ex) {
//                Log.e(TAG, "Exception in insert SQL", ex);
            }

        }

        public void onOpen(SQLiteDatabase db) {

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }

}
