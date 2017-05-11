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
    private String Tag = "test Receiver1";


    public void onReceive(Context context, Intent intent) {
        Log.i(Tag, "receiver start");
        Bundle extra = intent.getExtras();
        boolean setRec = intent.getBooleanExtra("alReceiver", Boolean.TRUE); // ※ True : defaultValue
        String chgMode = intent.getStringExtra("change mode"); // 바꿀 모드 불러옴
        String orgMode = intent.getStringExtra("original mode"); // 기존 모드 불러옴
        if(setRec) { // true일 때 시작 스케줄
            Intent i = new Intent(context,ScheduleSetActivity.class);
            PendingIntent p = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            // 여기에 바꿀 모드 chgMode를 넣으면 됩니다.
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
            // 여기에 기존 모드 orgMode를 넣으면 됩니다.
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
