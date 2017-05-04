package com.example.sjeong.groupapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
        String table = "CREATE TABLE MODE"+ "(NAME TEXT PRIMARY KEY NOT NULL,"+"STAR INTEGER NOT NULL,"+"CONTACT INTEGER NOT NULL,"+"UNKNOWN INTEGER NOT NULL,"+" TIME INTEGER NOT NULL,"+" COUNT INTEGER NOT NULL)";
        db.execSQL(table);
        Toast.makeText(dbcontext, "create db table", Toast.LENGTH_LONG).show();
        Log.i("test DB", "create db table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void ReadDB() { SQLiteDatabase db = getReadableDatabase(); }

    public void DeleteDB() {
        SQLiteDatabase db = getWritableDatabase();
        String insertdb = "DELETE FROM MODE";
        db.execSQL(insertdb);
        }


    public void insertMode(Mode mode){
        SQLiteDatabase db = getWritableDatabase();
        String insertdb = "INSERT INTO MODE("+"NAME, STAR, CONTACT, UNKNOWN, TIME, COUNT)"+"VALUES(?, ?, ?, ?, ?, ?)";
        db.execSQL(insertdb, new Object[]{mode.getName(), mode.getStar(), mode.getContact(), mode.getUnknown(), mode.getTime(), mode.getCount()});

        Toast.makeText(dbcontext, "insert", Toast.LENGTH_LONG).show();
        Log.i("test DB", "insert : " + mode.getName()+", "+mode.getStar()+", "+ mode.getContact()+", "+ mode.getUnknown()+", "+ mode.getTime()+", "+ mode.getCount());
    }

    public void updateMode(String originalname, Mode mode){
        SQLiteDatabase db = getWritableDatabase();
        String updatedb = "UPDATE MODE SET "+"NAME = ?, STAR =?, CONTACT =?, UNKNOWN =?, TIME =?, COUNT =?"+" WHERE NAME=?;";
        db.execSQL(updatedb, new Object[]{mode.getName().toString(), mode.getStar(), mode.getContact(), mode.getUnknown(), mode.getTime(), mode.getCount(), originalname});

        Toast.makeText(dbcontext, "update", Toast.LENGTH_LONG).show();
        Log.i("test DB", "update : " + mode.getName()+", "+mode.getStar()+", "+ mode.getContact()+", "+ mode.getUnknown()+", "+ mode.getTime()+", "+ mode.getCount());
    }

    public ArrayList<String> getModesName(){
        String string = "SELECT NAME FROM MODE";
        SQLiteDatabase db = getReadableDatabase();

        ArrayList<String> modes = new ArrayList<String>();

        Cursor cursor = db.rawQuery(string, null);
        if(cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                modes.add(cursor.getString(0));
                Log.i("test DBManager", cursor.getString(0));
            }
        }
        return modes;
    }

    public Mode getMode(String modename) {

        Log.i("test DBManager", "get mode1");
        String string = "SELECT NAME, STAR, CONTACT, UNKNOWN, TIME, COUNT FROM MODE";
        SQLiteDatabase db = getReadableDatabase();

        Log.i("test DBManager", "get mode2" + string );
        Cursor cursor = db.rawQuery(string, null);
        Mode mode=new Mode();

        Log.i("test DBManager", "get mode3");
        if(cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
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
        }
        else
            Log.i("test DBManager", "get mode null");

        return mode;
    }


}
class MyCursorAdapter extends CursorAdapter
{

    @SuppressWarnings("deprecation")
    public MyCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvName = (TextView) view.findViewById( R.id.tv_name );
        TextView tvAge = (TextView) view.findViewById( R.id.tv_age );

        String name = cursor.getString( cursor.getColumnIndex("STAR") );
        String age = cursor.getString( cursor.getColumnIndex("CONTACT") );

        Log.d("스트링 확인", name + ", " + age);

        tvName.setText( name );
        tvAge.setText( age );
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from( context );
        View v = inflater.inflate( R.layout.list_item, parent, false );
        return v;
    }

}
