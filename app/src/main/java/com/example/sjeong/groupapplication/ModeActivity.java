package com.example.sjeong.groupapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ModeActivity extends AppCompatActivity implements View.OnClickListener {

    private String Tag = "test ModeActive";
    private DBManager dbManager;
    private Button mode1;
    private Button mode2;
    //ArrayAdapter<String> arrayAdapter;
    ListAdapter listAdapter;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        // DB생성
        if (dbManager == null) {
            dbManager = new DBManager(ModeActivity.this, "AlarmCall", null, 1);
            //dbManager.DeleteDB();
            dbManager.ReadDB();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        Toast.makeText(this, "OnResume 호출", Toast.LENGTH_SHORT).show();
        ArrayList<String> modes = dbManager.getModesName();
        listAdapter = new ListAdapter(this, R.layout.listview, modes, onClickListener);
        list = (ListView) findViewById(R.id.listView);
        list.setAdapter(listAdapter);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ModeActivity.this, ModeSetActivity.class);

        switch (v.getId()) {
            case R.id.makemode:
                startActivity(intent);
                break;
            case R.id.option:
                intent = new Intent(ModeActivity.this, OptionActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /*
    AdapterView.OnItemClickListener ListViewItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parentView, View clickedView, int position, long id) {
            Intent intent = new Intent(ModeActivity.this, ModeSetActivity.class);
            intent.putExtra("Name",((TextView)clickedView).getText().toString());
            startActivity(intent);
        }
    };
    */

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String modename = v.getTag().toString();

            switch (v.getId()) {
                case R.id.itemmode:
                    Log.i("test tag", modename);
                    Intent intent = new Intent(ModeActivity.this, ModeSetActivity.class);
                    intent.putExtra("Name",modename);
                    startActivity(intent);
                    break;
                case R.id.select:
                    Log.i("test tag", modename);
                    SetNowMode(modename);
                    break;
                case R.id.delete:
                    Log.i("test tag", modename);
                    break;

            }
        }
    };

    public void SetNowMode(String name){

        Mode mode = new Mode();
        mode = dbManager.getMode(name);

        SharedPreferences preferences = getSharedPreferences("Mode", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("set", "on");
        editor.putString("name", mode.getName());
        editor.putInt("star", mode.getStar());
        editor.putInt("contact", mode.getContact());
        editor.putInt("unknown", mode.getUnknown());
        editor.putInt("time", mode.getTime());
        editor.putInt("count", mode.getCount());

        editor.commit();

        Log.i(Tag, "Set Now Mode");
    }

}