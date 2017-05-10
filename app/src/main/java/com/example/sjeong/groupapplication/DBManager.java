package com.example.sjeong.groupapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by SJeong on 2017-05-04.
 */

public class DBManager extends SQLiteOpenHelper {
    private Context dbcontext;

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        dbcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table = "CREATE TABLE MODE"+ "(NAME TEXT PRIMARY KEY NOT NULL,"+"STAR INTEGER NOT NULL,"+"CONTACT INTEGER NOT NULL,"+"UNKNOWN INTEGER NOT NULL,"+" TIME INTEGER NOT NULL,"+" COUNT INTEGER NOT NULL);";
        String table2 = "CREATE TABLE SCHEDULE(_id INTEGER PRIMARY KEY AUTOINCREMENT, START TEXT, END TEXT, " +
              "SUN BOOLEAN, MON BOOLEAN, TUE BOOLEAN,WED BOOLEAN, THU BOOLEAN, FRI BOOLEAN, SAT BOOLEAN, MODENAME TEXT, PREMODENAME TEXT);";
        db.execSQL(table);
        db.execSQL(table2);

        Toast.makeText(dbcontext, "create db table", Toast.LENGTH_LONG).show();
        Log.i("test DB", "create db table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void ReadDB() { SQLiteDatabase db = getReadableDatabase(); }

    public void DeleteDB() {  // DB 리셋용
        SQLiteDatabase db = getWritableDatabase();
        String insertdb = "DELETE FROM MODE;";
        db.execSQL(insertdb);
        insertdb = "DELETE FROM SCHEDULE;";
        db.execSQL(insertdb);
        }


    public void insertMode(Mode mode){
        SQLiteDatabase db = getWritableDatabase();
        String insertdb = "INSERT INTO MODE("+"NAME, STAR, CONTACT, UNKNOWN, TIME, COUNT)"+" VALUES(?, ?, ?, ?, ?, ?);";
        db.execSQL(insertdb, new Object[]{mode.getName(), mode.getStar(), mode.getContact(), mode.getUnknown(), mode.getTime(), mode.getCount()});
        Toast.makeText(dbcontext, "insert", Toast.LENGTH_LONG).show();
        Log.i("test DB", "insert : " + mode.getName()+", "+mode.getStar()+", "+ mode.getContact()+", "+ mode.getUnknown()+", "+ mode.getTime()+", "+ mode.getCount());
    }

    public void insertSchedule(Schedule schedule){
        SQLiteDatabase db = getWritableDatabase();
        String insertdb = "INSERT INTO SCHEDULE(_id, START, END, SUN, MON, TUE, WED, THU, FRI, SAT, MODENAME, PREMODENAME) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        db.execSQL(insertdb, new Object[]{null, schedule.getStart(), schedule.getEnd(), schedule.getSun().booleanValue(), schedule.getMon().booleanValue(), schedule.getTue().booleanValue(), schedule.getWed().booleanValue(), schedule.getThu().booleanValue(), schedule.getFri().booleanValue(), schedule.getSat().booleanValue(), schedule.getModename().toString(), schedule.getPremodename().toString()});
        Log.i("test DB", "insert : " + schedule.getStart()+", "+schedule.getEnd()+", "+schedule.getModename().toString()+", "+ schedule.getPremodename().toString());
    }

    public void updateMode(String originalname, Mode mode){
        SQLiteDatabase db = getWritableDatabase();
        String updatedb = "UPDATE MODE SET "+"NAME = ?, STAR =?, CONTACT =?, UNKNOWN =?, TIME =?, COUNT =?"+" WHERE NAME=?;";
        db.execSQL(updatedb, new Object[]{mode.getName().toString(), mode.getStar(), mode.getContact(), mode.getUnknown(), mode.getTime(), mode.getCount(), originalname});

        Toast.makeText(dbcontext, "update", Toast.LENGTH_LONG).show();
        Log.i("test DB", "update : " + mode.getName()+", "+mode.getStar()+", "+ mode.getContact()+", "+ mode.getUnknown()+", "+ mode.getTime()+", "+ mode.getCount());
    }

    public void updateSchedule(String originalname, Schedule schedule){
        SQLiteDatabase db = getWritableDatabase();
        String updatedb = "UPDATE SCHEDULE SET ORG_RING =?, CHG_RING =?, START =?, END =?, SUN =?, MON =?, TUE =?, WED =?, THU =?, FRI =?, SAT =?, MODENAME =? WHERE NAME =?;";
        //db.execSQL(updatedb, new Object[]{schedule.getOrg_ring().toString(), schedule.getChg_ring().toString(), schedule.getStart(), schedule.getEnd(), schedule.getSun().booleanValue(), schedule.getMon().booleanValue(), schedule.getTue().booleanValue(), schedule.getWed().booleanValue(), schedule.getThu().booleanValue(), schedule.getFri().booleanValue(), schedule.getSat().booleanValue(), schedule.getModename().toString(), originalname});
    }

    public void deleteMode(String modename){
        SQLiteDatabase db  = getWritableDatabase();
        String deletetable = "DELETE FROM MODE WHERE NAME = ?;";
        db.execSQL(deletetable, new Object[]{modename});
        Log.i("test DB", "delete mode"+modename);
    }

    public void deleteSchedule(String schedulename, Schedule schedule){
        SQLiteDatabase db = getWritableDatabase();
        String deletetable = "DELETE FROM SCHEDULE WHERE SCHEDULE = ?;";
        //db.execSQL(deletetable, new Object[]{schedule.getName().toString(), schedulename});
    }

    public ArrayList<String> getModesName(){
        String string = "SELECT NAME FROM MODE;";
        SQLiteDatabase db = getReadableDatabase();

        ArrayList<String> modes = new ArrayList<String>();

        Cursor cursor = db.rawQuery(string, null);
        if(cursor.moveToFirst()) {
            do {
                modes.add(cursor.getString(0));
                Log.i("test DBManager", cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        return modes;
    }

    public ArrayList<Schedule> getSchedules(){
        String string = "SELECT * FROM SCHEDULE;";
        SQLiteDatabase db = getReadableDatabase();

        ArrayList<Schedule> schedules = new ArrayList<Schedule>();

        Cursor cursor = db.rawQuery(string, null);
        if(cursor.moveToFirst()) {
            do {
                Schedule schedule = new Schedule();

                schedule.setStart(cursor.getString(1));
                schedule.setEnd(cursor.getString(2));
                schedule.setModename(cursor.getString(10));
                schedule.setPremodename(cursor.getString(11));

                schedules.add(schedule);

                Log.i("test DBManager", cursor.getString(10));
                Log.i("test DBManager", cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        return schedules;
    }

    public Mode getMode(String modename) {

        String string = "SELECT NAME, STAR, CONTACT, UNKNOWN, TIME, COUNT FROM MODE;";
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(string, null);
        Mode mode=new Mode();

        if(cursor.moveToFirst())
        {
            do {
                Log.i("test DBManager", cursor.getString(0));
                if (cursor.getString(0).equals(modename)) {
                    mode.setName(cursor.getString(0));
                    mode.setStar(cursor.getInt(1));
                    mode.setContact(cursor.getInt(2));
                    mode.setUnknown(cursor.getInt(3));
                    mode.setTime(cursor.getInt(4));
                    mode.setCount(cursor.getInt(5));
                    return mode;
                }
            }
                while (cursor.moveToNext());
        }
        else
            Log.i("test DBManager", "get mode null");

        return mode;
    }

    public Schedule getSchedule(int position){
        String string = "SELECT * FROM SCHEDULE;";
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(string, null);
        Schedule schedule=new Schedule();

        int cusorposition=0;

        if(cursor.moveToFirst())
        {
            do {
                if (cusorposition==position) {
                    schedule.setStart(cursor.getString(1));
                    schedule.setEnd(cursor.getString(2));
                    schedule.setModename(cursor.getString(10));
                    schedule.setPremodename(cursor.getString(11));
                    Log.i("test DBManager", "get schedule "+cursor.getString(1)+", "+cursor.getString(2)+", "+cursor.getString(10));
                    return schedule;
                }
                cusorposition++;
            }
            while (cursor.moveToNext());
        }
        else
            Log.i("test DBManager", "get mode null");

        return null;
    }

}