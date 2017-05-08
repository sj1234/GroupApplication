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
        db.execSQL(table);

        String table2 = "CREATE TABLE SCHEDULE(NAME TEXT PRIMARY KEY NOT NULL, ORG_RING TEXT, CHG_RING TEXT, START TEXT, END TEXT, " +
                "BOOLEAN SUN, BOOLEAN MON, BOOLEAN TUE, BOOLEAN WED, BOOLEAN THU, BOOLEAN FRI, BOOLEAN SAT, MODENAME TEXT);";
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
        String insertdb = "INSERT INTO SCHEDULE(NAME, ORG_RING, CHG_RING, START, END, SUN, MON, TUE, WED, THU, FRI, SAT, MODENAME) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        db.execSQL(insertdb, new Object[]{schedule.getName().toString(), schedule.getOrg_ring().toString(), schedule.getChg_ring().toString(), schedule.getStart(), schedule.getEnd(), schedule.getSun().booleanValue(), schedule.getMon().booleanValue(), schedule.getTue().booleanValue(), schedule.getWed().booleanValue(), schedule.getThu().booleanValue(), schedule.getFri().booleanValue(), schedule.getSat().booleanValue(), schedule.getModename().toString()});
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
        String updatedb = "UPDATE SCHEDULE SET NAME = ?, ORG_RING =?, CHG_RING =?, START =?, END =?, SUN =?, MON =?, TUE =?, WED =?, THU =?, FRI =?, SAT =?, MODENAME =? WHERE NAME =?;";
        db.execSQL(updatedb, new Object[]{schedule.getName().toString(), schedule.getOrg_ring().toString(), schedule.getChg_ring().toString(), schedule.getStart(), schedule.getEnd(), schedule.getSun().booleanValue(), schedule.getMon().booleanValue(), schedule.getTue().booleanValue(), schedule.getWed().booleanValue(), schedule.getThu().booleanValue(), schedule.getFri().booleanValue(), schedule.getSat().booleanValue(), schedule.getModename().toString(), originalname});
    }

    public void deleteMode(String modename, Mode mode){
        SQLiteDatabase db  = getWritableDatabase();
        String deletetable = "DELETE FROM MODE WHERE NAME = ?;";
        db.execSQL(deletetable, new Object[]{mode.getName().toString(), modename});
    }

    public void deleteSchedule(String schedulename, Schedule schedule){
        SQLiteDatabase db = getWritableDatabase();
        String deletetable = "DELETE FROM SCHEDULE WHERE SCHEDULE = ?;";
        db.execSQL(deletetable, new Object[]{schedule.getName().toString(), schedulename});
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

    public ArrayList<String> getArray(){   // 아직 완성안됨
        String string = "SELECT STAR, CONTACT, UNKNOWN FROM MODE WHERE NAME= ?;";
        String getExtra[] = null;
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> modes = new ArrayList<String>();
        Cursor cursor = db.rawQuery(string, getExtra); // getExtra 에 putExtra값 받아옴
        if(cursor.moveToFirst()) {
            do {
                modes.add(cursor.getString(0));
                modes.add(cursor.getString(1));
                modes.add(cursor.getString(2));
                Log.i("test DBManager", cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        return modes;
    }

    public Mode getMode(String modename) {

        Log.i("test DBManager", "get mode1");
        String string = "SELECT NAME, STAR, CONTACT, UNKNOWN, TIME, COUNT FROM MODE;";
        SQLiteDatabase db = getReadableDatabase();

        Log.i("test DBManager", "get mode2" + string );
        Cursor cursor = db.rawQuery(string, null);
        Mode mode=new Mode();

        Log.i("test DBManager", "get mode3");
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
                    Log.i("test DBManager", "get mode4");
                    return mode;
                }
            }
                while (cursor.moveToNext());
        }
        else
            Log.i("test DBManager", "get mode null");

        return mode;
    }

}