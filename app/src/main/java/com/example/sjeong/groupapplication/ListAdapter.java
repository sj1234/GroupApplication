package com.example.sjeong.groupapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SJeong on 2017-05-08.
 */

public class ListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<String> arraylist;
    private String name;
    private View.OnClickListener onClickListener;

    public ListAdapter(Context context, int layout, ArrayList<String> arraylist, View.OnClickListener onClickListener){
            this.context = context;
            this.layout = layout;
            this.arraylist = arraylist;
            this.onClickListener = onClickListener;
        }

    //리스트 객체 내의 item의 갯수를 반환해주는 함수. 리스트 객체의 size를 반환해주면된다
    @Override
    public int getCount() {
        return arraylist.size();
    }

    //전달받은 position의 위치에 해당하는 리스트 객체의 item를 객체 형태로 반환해주는 함수.
    @Override
    public Object getItem(int position) {
        return arraylist.get(position);
    }

    //전달받은 position의 위치에 해당하는 리스트 객체의 item의 row ID를 반환해주는 함수.
    @Override
    public long getItemId(int position) {
        return position;
    }

    // ListView의 항목들을 출력하는 함수 position : 해당되는 항목의 Adapter에서의 위치값  convertView : 재사용할 항목의 View   parent : 항목의 View들을 포함하고 있는 ListView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.itemmode);
        Button buttonselect = (Button)convertView.findViewById(R.id.select) ;
        Button buttondelete = (Button)convertView.findViewById(R.id.delete) ;

        name = arraylist.get(position).toString();

        Log.i("test listbutton", arraylist.get(position).toString());
        textView.setText(arraylist.get(position).toString());
        if(onClickListener != null) {
            textView.setTag(arraylist.get(position).toString());
            textView.setOnClickListener(onClickListener);

            buttonselect.setTag(arraylist.get(position).toString());
            buttonselect.setOnClickListener(onClickListener);

            buttondelete.setTag(arraylist.get(position).toString());
            buttondelete.setOnClickListener(onClickListener);
        }

        return convertView;

    }
}
