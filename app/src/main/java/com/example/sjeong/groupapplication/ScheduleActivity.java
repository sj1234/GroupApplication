package com.example.sjeong.groupapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;


public class ScheduleActivity extends AppCompatActivity {
    static AlarmManager am;
    static PendingIntent amIntent;
    static PendingIntent amIntent2;
    private ToggleButton toggleSun,toggleMon,toggleTue,toggleWed,toggleThu,toggleFri,toggleSat;
    private String Tag = "test Alarm1";
    private Switch aSwitch;
    View v1 = null;
    View v2 = null;
    static Calendar calNow1 = Calendar.getInstance();   // ?꾩옱 ?쒓컙???꾪븳 Calendar 媛앹껜瑜?援ы븳??
    static Calendar calSet = (Calendar) calNow1.clone();   // 諛붾줈 ?꾩뿉??援ы븳 媛앹껜瑜?蹂듭젣 ?쒕떎.
    static Calendar calNow2 = Calendar.getInstance();   // ?꾩옱 ?쒓컙???꾪븳 Calendar 媛앹껜瑜?援ы븳??
    static Calendar calReset = (Calendar) calNow2.clone();   // 諛붾줈 ?꾩뿉??援ы븳 媛앹껜瑜?蹂듭젣 ?쒕떎.

    private DBManager dbManager;
    private Schedule schedule;
    private String ringer_chg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        toggleSun = (ToggleButton) findViewById(R.id.toggle_sun);
        toggleMon = (ToggleButton) findViewById(R.id.toggle_mon);
        toggleTue = (ToggleButton) findViewById(R.id.toggle_tue);
        toggleWed = (ToggleButton) findViewById(R.id.toggle_wed);
        toggleThu = (ToggleButton) findViewById(R.id.toggle_thu);
        toggleFri = (ToggleButton) findViewById(R.id.toggle_fri);
        toggleSat = (ToggleButton) findViewById(R.id.toggle_sat);
        aSwitch =(Switch)findViewById(R.id.amSw);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    Toast.makeText(ScheduleActivity.this, "?ㅼ쐞移?ON", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ScheduleActivity.this, "?ㅼ쐞移?OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // DB?앹꽦
        if (dbManager == null) {
            dbManager = new DBManager(ScheduleActivity.this, "AlarmCall", null, 1);
            //dbManager.DeleteDB();
            dbManager.ReadDB();
        }
        schedule = new Schedule();
    }

    public void onButton(final View v) {
        switch (v.getId()) {
            case R.id.timeSet :  // ?쒖옉?쒓컙
                Calendar c = Calendar.getInstance();    // Calendar 媛앹껜瑜?援ы븳??
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        ScheduleActivity.this, timeListener1,                                           // 由ъ뒪??異붽?
                        c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);  //?꾩옱 ?쒓컙怨?遺꾩쓣 TimePickerDialog???ㅼ젙
                timePickerDialog.setTitle("?뚮엺 ?쒓컙 ?ㅼ젙");  // TimePickerDialog ?쒕ぉ???뺥븳??
                timePickerDialog.show();   // TimePickerDialog瑜??붾㈃??蹂댁씤??
                Log.i(Tag, "press timeSet button");
                break;
            case R.id.timeReset : // 醫낅즺?쒓컙
                Calendar c2 = Calendar.getInstance();    // Calendar 媛앹껜瑜?援ы븳??
                TimePickerDialog timePickerDialog2 = new TimePickerDialog(
                        ScheduleActivity.this, timeListener2,                                           // 由ъ뒪??異붽?
                        c2.get(Calendar.HOUR_OF_DAY), c2.get(Calendar.MINUTE), false);  //?꾩옱 ?쒓컙怨?遺꾩쓣 TimePickerDialog???ㅼ젙
                timePickerDialog2.setTitle("?뚮엺 ?쒓컙 ?ㅼ젙");  // TimePickerDialog ?쒕ぉ???뺥븳??
                timePickerDialog2.show();   // TimePickerDialog瑜??붾㈃??蹂댁씤??
                Log.i(Tag, "press timeReset button");
                break;
            case  R.id.Clear :
                clearAlarm();
                Log.i(Tag, "press Clear button");
                break;
        }
    }


    OnTimeSetListener timeListener1 = new OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);   // ?쒓컙 ?ㅼ젙
            calSet.set(Calendar.MINUTE, minute);        // 遺??ㅼ젙
            calSet.set(Calendar.SECOND, 0);               // 珥덈뒗 '0'?쇰줈 ?ㅼ젙
            calSet.set(Calendar.MILLISECOND, 0);       // 諛由?珥덈룄  '0' ?쇰줈 ?ㅼ젙
            if (calSet.compareTo(calNow1) <= 0) {            // ?ㅼ젙???쒓컙怨??꾩옱 ?쒓컙 鍮꾧탳
                // 留뚯빟 ?ㅼ젙???쒓컙???꾩옱 ?쒓컙蹂대떎 ?댁쟾?대㈃
                calSet.add(Calendar.DATE, 1);  // ?ㅼ젙 ?쒓컙???섎（瑜??뷀븳??
            }
            setAlarm(calSet,v1);  // 二쇱뼱吏??쒓컙?쇰줈 ?뚮엺???ㅼ젙?쒕떎.
            Log.i(Tag, "settime listener finish");
            EditText editText1=(EditText) findViewById(R.id.amStart);
            editText1.setText("紐⑤뱶 ?쒖옉 ?쒓컙? "+ calSet.getTime());
        }
    };

    OnTimeSetListener timeListener2 = new OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            calReset.set(Calendar.HOUR_OF_DAY, hourOfDay);   // ?쒓컙 ?ㅼ젙
            calReset.set(Calendar.MINUTE, minute);        // 遺??ㅼ젙
            calReset.set(Calendar.SECOND, 0);               // 珥덈뒗 '0'?쇰줈 ?ㅼ젙
            calReset.set(Calendar.MILLISECOND, 0);       // 諛由?珥덈룄  '0' ?쇰줈 ?ㅼ젙
            if (calReset.compareTo(calNow2) <= 0) {            // ?ㅼ젙???쒓컙怨??꾩옱 ?쒓컙 鍮꾧탳
                // 留뚯빟 ?ㅼ젙???쒓컙???꾩옱 ?쒓컙蹂대떎 ?댁쟾?대㈃
                calReset.add(Calendar.DATE, 1);  // ?ㅼ젙 ?쒓컙???섎（瑜??뷀븳??
            }
            resetAlarm(calReset,v2);  // 二쇱뼱吏??쒓컙?쇰줈 ?뚮엺???ㅼ젙?쒕떎.
            Log.i(Tag, "reset time listener finish");
            EditText editText1=(EditText) findViewById(R.id.amFinish);
            editText1.setText("紐⑤뱶 ?쒖옉 ?쒓컙? "+ calReset.getTime());
        }
    };

    private void setAlarm(Calendar targetCal,View v) {
        Log.i(Tag, "reset Alarm start");
        Context context = getApplicationContext();
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // PendingIntent
        Intent intent = new Intent(context, AlarmReceiver.class);
        /*
        ringer_chg = schedule.getChg_ring_ring();
       intent.putExtra("changed",ringer_chg);
       */
        amIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        // AlarmManager???뚮엺 ?쒓컙 ?ㅼ젙
        am.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), amIntent);
        Toast.makeText(context, "Reset time is " + targetCal.getTime(), Toast.LENGTH_SHORT).show();
    }


    private void resetAlarm(Calendar targetCal,View v) {
        Log.i(Tag, "reset Alarm start");
        Context context = getApplicationContext();
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // PendingIntent
        Intent intent = new Intent(context, AlarmReceiver.class);
        // intent.putExtra("ringer mode",org_ring);
        amIntent2 = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        // AlarmManager???뚮엺 ?쒓컙 ?ㅼ젙
        am.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), amIntent2);
        Toast.makeText(context, "Reset time is " + targetCal.getTime(), Toast.LENGTH_SHORT).show();
    }

    private void clearAlarm(){ // ?뚮엺 ?댁젣
        if(amIntent == null){
            Log.i(Tag, "There is no alarm");
            Toast.makeText(getApplicationContext(), "There is no alarm " , Toast.LENGTH_SHORT).show();
        }
        else {
            Context context = getApplicationContext();
            am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            amIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            amIntent2 = PendingIntent.getBroadcast(context,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            am.cancel(amIntent);
            am.cancel(amIntent2);
            amIntent=null;
            amIntent2=null;
            am=null;
            Log.i(Tag, "Clear Alarm");
        }
    }
}
