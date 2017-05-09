package com.example.sjeong.groupapplication;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;

public class ScheduleSetActivity extends AppCompatActivity {

    private AlarmManager am;
    private String Tag = "test ScheduleSetActivity";
    private ToggleButton toggleSun,toggleMon,toggleTue,toggleWed,toggleThu,toggleFri,toggleSat;
    private Switch aSwitch;
    private DBManager dbManager;
    private Schedule schedule;

    Calendar calNow1 = Calendar.getInstance();
    Calendar calSet = (Calendar) calNow1.clone();
    Calendar calNow2 = Calendar.getInstance();
    Calendar calReset = (Calendar) calNow2.clone();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_set);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
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
                    Toast.makeText(ScheduleSetActivity.this, "?ㅼ쐞移?ON", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ScheduleSetActivity.this, "?ㅼ쐞移?OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (dbManager == null) {
            dbManager = new DBManager(ScheduleSetActivity.this, "AlarmCall", null, 1);
            dbManager.ReadDB();
        }

        schedule = new Schedule();

        ArrayList<String> arraylist = dbManager.getModesName();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arraylist);
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                schedule.setModename(""+parent.getItemAtPosition(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        SharedPreferences preferences = getSharedPreferences("Mode", Activity.MODE_PRIVATE);
        schedule.setPremodename(preferences.getString("name",""));

        final EditText schedulename = (EditText) findViewById(R.id.schedulename);

        Button clear = (Button)findViewById(R.id.Clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                schedule.setName(schedulename.getText().toString());
                schedule.setMon(false);
                schedule.setTue(false);
                schedule.setWed(false);
                schedule.setThu(false);
                schedule.setFri(false);
                schedule.setSat(false);
                schedule.setSun(false);
                Toast.makeText(ScheduleSetActivity.this, "저장", Toast.LENGTH_SHORT).show();
                dbManager.insertSchedule(schedule);
            }
        });
    }

    public void onButton(final View v) {
        switch (v.getId()) {
            case R.id.timeSet :
                Calendar c = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        ScheduleSetActivity.this, timeListener1,
                        c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
                timePickerDialog.setTitle("START TIME");
                timePickerDialog.show();
                break;
            case R.id.timeReset :
                Calendar c2 = Calendar.getInstance();
                TimePickerDialog timePickerDialog2 = new TimePickerDialog(
                        ScheduleSetActivity.this, timeListener2,
                        c2.get(Calendar.HOUR_OF_DAY), c2.get(Calendar.MINUTE), false);
                timePickerDialog2.setTitle("END TIME");
                timePickerDialog2.show();
                break;
            default:
                break;
        }
    }

    TimePickerDialog.OnTimeSetListener timeListener1 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);
            if (calSet.compareTo(calNow1) <= 0) {
                calSet.add(Calendar.DATE, 1);
            }
            schedule.setStart(calSet.toString());

            TextView Text1=(TextView) findViewById(R.id.amStart);
            Text1.setText("시작시간"+ calSet.getTime());
        }
    };

    TimePickerDialog.OnTimeSetListener timeListener2 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            calReset.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calReset.set(Calendar.MINUTE, minute);
            calReset.set(Calendar.SECOND, 0);
            calReset.set(Calendar.MILLISECOND, 0);
            if (calReset.compareTo(calNow2) <= 0) {
                calReset.add(Calendar.DATE, 1);
            }

            schedule.setEnd(calReset.toString());
            TextView Text1=(TextView) findViewById(R.id.amFinish);
            Text1.setText("끝나는 시간 "+ calReset.getTime());
        }
    };
}
