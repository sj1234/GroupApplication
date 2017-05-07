package com.example.sjeong.groupapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ModeActivity extends AppCompatActivity implements View.OnClickListener {

    private String Tag = "test ModeActive";
    private DBManager dbManager;
    private Button mode1;
    private Button mode2;
    ArrayAdapter<String> arrayAdapter;

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

        mode1 = (Button) this.findViewById(R.id.mode1);
        mode1.setOnClickListener((View.OnClickListener) this);

        mode2 = (Button) this.findViewById(R.id.mode2);
        mode2.setOnClickListener((View.OnClickListener) this);

        ArrayList<String> modes = dbManager.getModesName();
        Log.i(Tag, "mod size" + modes.size());
        if (modes.size() > 1) {
            mode1.setText(modes.get(0).toString());
            mode2.setText(modes.get(1).toString());
        }
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, modes);

        ListView list = (ListView) findViewById(R.id.listView);


        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(ListViewItemClickListener);
        Button makemode = (Button) this.findViewById(R.id.makemode);
        makemode.setOnClickListener((View.OnClickListener) this);

        Button option = (Button) this.findViewById(R.id.option);
        option.setOnClickListener((View.OnClickListener) this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ModeActivity.this, ModeSetActivity.class);

        switch (v.getId()) {
            case R.id.mode1:
                intent.putExtra("Name", mode1.getText());
                Log.i(Tag, "mod name" + mode1.getText());
                startActivity(intent);
                break;
            case R.id.mode2:
                intent.putExtra("Name", mode2.getText());
                startActivity(intent);
                break;
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

    AdapterView.OnItemClickListener ListViewItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parentView, View clickedView, int position, long id) {
            Intent intent = new Intent(ModeActivity.this, ModeSetActivity.class);
            intent.putExtra("Name",((TextView)clickedView).getText().toString());
            startActivity(intent);
        }
    };
}