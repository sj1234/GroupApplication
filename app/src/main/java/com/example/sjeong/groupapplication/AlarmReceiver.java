package com.example.sjeong.groupapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver{
    private String Tag = "test Reciver1";


    public void onReceive(Context context, Intent intent) {// 5월 4일 AVD 한 1초 뒤에 시작.
        Log.i(Tag, "reset receiver start");
        Bundle extra = intent.getExtras();
        boolean setRec = intent.getBooleanExtra("alReceiver", Boolean.TRUE); // ※ True : defaultValue
        if(setRec) { // true일 때 setAlarm으로
            Intent i = new Intent(context,ScheduleSetActivity.class);
            PendingIntent p = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            //if (week[cal.get(Calendar.DAY_OF_WEEK)]) {
            NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(android.R.drawable.stat_notify_chat).setTicker("HETT").setWhen(System.currentTimeMillis())
                    .setContentTitle("Set Alarm Changed").setContentText("Set")
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(p).setAutoCancel(true);

            notificationmanager.notify(1, builder.build());
            Log.i(Tag, "set receiver finish");
        }
        else if(!setRec){ // false일 때 resetAlarm.
            Intent i = new Intent(context, ScheduleSetActivity.class);
            PendingIntent p = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            //if (week[cal.get(Calendar.DAY_OF_WEEK)]) {
            NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(android.R.drawable.stat_notify_chat).setTicker("HETT").setWhen(System.currentTimeMillis())
                    .setContentTitle("Reset Alarm Changed").setContentText("Reset")
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(p).setAutoCancel(true);

            notificationmanager.notify(1, builder.build());
            Log.i(Tag, "reset receiver finish");
        }

    }
}
