package com.example.sjeong.groupapplication;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver{
    private String Tag = "test Receiver1";
    private DBManager dbManager;

    public void onReceive(Context context, Intent intent) {

        // DB생성
        if (dbManager == null) {
            dbManager = new DBManager(context, "AlarmCall", null, 1);
            dbManager.ReadDB();
        }

        Log.i(Tag, "receiver start");
        Bundle extra = intent.getExtras();
        int[] week = intent.getIntArrayExtra("weekday");
        Log.i("AlarmReceiver.java | onReceive", "|일 : " + week[Calendar.SUNDAY]); //Calendar.SUNDAY == 1
        Log.i("AlarmReceiver.java | onReceive", "|월 : " + week[Calendar.MONDAY]);
        Log.i("AlarmReceiver.java | onReceive", "|화 : " + week[Calendar.TUESDAY]);
        Log.i("AlarmReceiver.java | onReceive", "|수 : " + week[Calendar.WEDNESDAY]);
        Log.i("AlarmReceiver.java | onReceive", "|목 : " + week[Calendar.THURSDAY]);
        Log.i("AlarmReceiver.java | onReceive", "|금 : " + week[Calendar.FRIDAY]);
        Log.i("AlarmReceiver.java | onReceive", "|토 : " + week[Calendar.SATURDAY]);

        Calendar cal = Calendar.getInstance();
        Log.i("AlarmReceiver.java | onReceive", "|" + cal.get(Calendar.DAY_OF_WEEK) + "|");
        // 해당 요일이 아니면 리턴한다.

        if (week[cal.get(Calendar.DAY_OF_WEEK)]==0)
            return;

        boolean setRec = intent.getBooleanExtra("alReceiver", Boolean.TRUE); // ※ True : defaultValue
        String chgMode = intent.getStringExtra("change mode"); // 바꿀 모드 불러옴
        String orgMode = intent.getStringExtra("original mode"); // 기존 모드 불러옴

        if(setRec) { // true일 때 시작 스케줄
            Intent i = new Intent(context,ScheduleSetActivity.class);
            PendingIntent p = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            //바꿀 모드 chgMode
            Mode mode = dbManager.getMode(chgMode);
            SharedPreferences preferences = context.getSharedPreferences("Mode", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("set", "on");
            editor.putString("name", mode.getName());
            editor.putInt("star", mode.getStar());
            editor.putInt("contact", mode.getContact());
            editor.putInt("unknown", mode.getUnknown());
            editor.putInt("time", mode.getTime());
            editor.putInt("count", mode.getCount());
            editor.commit();

            // 위젯변경
            Intent wintent = new Intent(context, AppWidget.class);
            wintent.setAction("com.example.sjeong.groupapplication.CHANGE");
            PendingIntent pendindintent = PendingIntent.getBroadcast(context, 0, wintent, 0);
            try {
                pendindintent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }

            Log.i(Tag, "changing to " + chgMode);
            NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(android.R.drawable.stat_notify_chat).setTicker("HETT").setWhen(System.currentTimeMillis())
                    .setContentTitle("Schedule starts").setContentText("change mode")
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(p).setAutoCancel(true);

            notificationmanager.notify(1, builder.build());
            Log.i(Tag, "Start receiver finish");
        }
        else if(!setRec){ // false일 때 종료 스케줄
            Intent i = new Intent(context, ScheduleSetActivity.class);
            PendingIntent p = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            // 기존 모드 orgMode
            SharedPreferences preferences = context.getSharedPreferences("Mode", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            if(orgMode==null){
                editor.putString("set", "off");
                editor.commit();
            }
            else {
                Mode mode = dbManager.getMode(orgMode);

                editor.putString("set", "on");
                editor.putString("name", mode.getName());
                editor.putInt("star", mode.getStar());
                editor.putInt("contact", mode.getContact());
                editor.putInt("unknown", mode.getUnknown());
                editor.putInt("time", mode.getTime());
                editor.putInt("count", mode.getCount());
                editor.commit();
            }

            // 위젯변경
            Intent wintent = new Intent(context, AppWidget.class);
            wintent.setAction("com.example.sjeong.groupapplication.CHANGE");
            PendingIntent pendindintent = PendingIntent.getBroadcast(context, 0, wintent, 0);
            try {
                pendindintent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }

            Log.i(Tag, "back to " + orgMode);
            NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(android.R.drawable.stat_notify_chat).setTicker("HETT").setWhen(System.currentTimeMillis())
                    .setContentTitle("Schedule ends").setContentText("back to original mode")
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(p).setAutoCancel(true);

            notificationmanager.notify(1, builder.build());
            Log.i(Tag, "End receiver finish");
        }

    }
}
