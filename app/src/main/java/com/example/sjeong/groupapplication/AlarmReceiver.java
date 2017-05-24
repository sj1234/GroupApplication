package com.example.sjeong.groupapplication;

import android.app.Activity;
import android.app.AlarmManager;
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
    private AlarmManager am;
    private DBManager dbManager;

    public void onReceive(Context context, Intent intent) {

        // DB생성
        if (dbManager == null) {
            dbManager = new DBManager(context, "AlarmCall", null, 1);
            dbManager.ReadDB();
        }
        Bundle extra = intent.getExtras();
        int pos = intent.getIntExtra("position", 0); // 리스브뷰 포지션 default 시 0
        int req = intent.getIntExtra("reqid",0); // 알람 requestcode default 시 0
        boolean setRec = intent.getBooleanExtra("alReceiver", Boolean.TRUE); // ※ True : defaultValue
        String chgMode = intent.getStringExtra("change mode"); // 바꿀 모드 불러옴
        String orgMode = intent.getStringExtra("original mode"); // 기존 모드 불러옴

        Schedule schedule = dbManager.getSchedule(pos);
        String starttime = schedule.getStart().toString();
        String endtime = schedule.getEnd().toString();
        String[] start = starttime.split(":");
        String[] end = endtime.split(":");
        Calendar calStart = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();
        int starttimeHOUR = Integer.parseInt(start[0]);
        int starttimeMINUTE = Integer.parseInt(start[1]);
        int endtimeHOUR = Integer.parseInt(end[0]);
        int endtimeMINUTE = Integer.parseInt(end[1]);
        calStart.set(calStart.get(Calendar.YEAR), calStart.get(Calendar.MONTH), calStart.get(Calendar.DATE), starttimeHOUR, starttimeMINUTE, 0);
        Log.i(Tag, " onbutton setting 시간 " + starttimeHOUR + "  setting 분" + starttimeMINUTE);
        Log.i(Tag, " 스케줄 시작 시간 :  " + calStart.getTimeInMillis());
        calEnd.set(calEnd.get(Calendar.YEAR), calEnd.get(Calendar.MONTH), calEnd.get(Calendar.DATE), endtimeHOUR, endtimeMINUTE, 0);
        Log.i(Tag, " onbutton setting 시간 :  " + endtimeHOUR + "  setting 분 : " + endtimeMINUTE);
        Log.i(Tag, " 스케줄 종료 시간 :  " + calEnd.getTimeInMillis());

        Log.i(Tag, "receiver start");

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

        if (week[cal.get(Calendar.DAY_OF_WEEK)] == 1) { // 해당 요일이라면


            if (setRec) { // true일 때 시작 스케줄
                am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                PendingIntent pend = PendingIntent.getBroadcast(context, req, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                am.cancel(pend);
                am.setExact(AlarmManager.RTC_WAKEUP, calStart.getTimeInMillis()+ 24*60*60*1000, pend); // 24시간 후에 다시 set한다.
                calStart.setTimeInMillis(calStart.getTimeInMillis()+ (24*60*60*1000)); // db에 하루 후로 스케줄 저장.
                schedule.setStart(calStart.getTime().toString());
                Intent i = new Intent(context, ScheduleSetActivity.class);
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
            } else if (!setRec) { // false일 때 종료 스케줄
                am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                PendingIntent pend = PendingIntent.getBroadcast(context, req, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                am.cancel(pend);
                am.setExact(AlarmManager.RTC_WAKEUP, calEnd.getTimeInMillis()+ 24*60*60*1000, pend); // 24시간 후로 다시 알람 set한다.
                calEnd.setTimeInMillis(calEnd.getTimeInMillis()+ (24*60*60*1000));
                schedule.setEnd(calEnd.getTime().toString()); // db에 하루 후로 스케줄 저장.
                Intent i = new Intent(context, ScheduleSetActivity.class);
                PendingIntent p = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                // 기존 모드 orgMode
                SharedPreferences preferences = context.getSharedPreferences("Mode", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                if (orgMode == null) {
                    editor.putString("set", "off");
                    editor.commit();
                } else {
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
        else if(week[cal.get(Calendar.DAY_OF_WEEK)] == 0){// 해당 요일이 아니라면
            if(setRec) {
                am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                PendingIntent pend = PendingIntent.getBroadcast(context, req, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                am.cancel(pend);
                am.setExact(AlarmManager.RTC_WAKEUP, calStart.getTimeInMillis()+ 24*60*60*1000, pend); // 24시간 후에 다시 set한다.
                calStart.setTimeInMillis(calStart.getTimeInMillis()+ (24*60*60*1000)); // db에 하루 후로 스케줄 저장.
                schedule.setStart(calStart.getTime().toString());
            }
            else if(!setRec){
                am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                PendingIntent pend = PendingIntent.getBroadcast(context, req, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                am.cancel(pend);
                am.setExact(AlarmManager.RTC_WAKEUP, calEnd.getTimeInMillis()+ 24*60*60*1000, pend); // 24시간 후로 다시 알람 set한다.
                calEnd.setTimeInMillis(calEnd.getTimeInMillis()+ (24*60*60*1000));
                schedule.setEnd(calEnd.getTime().toString()); // db에 하루 후로 스케줄 저장.
            }
        }

    }
}

