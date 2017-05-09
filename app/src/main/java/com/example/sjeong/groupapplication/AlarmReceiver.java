package com.example.sjeong.groupapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;


public class AlarmReceiver extends BroadcastReceiver {
    private String Tag = "test Reciver1";


    private Context mContext;
    private AudioManager myAudioManager;
    public AlarmReceiver(Context context) {
        this.mContext = context;
        myAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
    }

    public void onReceive(Context context, Intent intent) {// 5??4??AVD ??1珥??ㅼ뿉 ?쒖옉.
        Log.i(Tag, "reset receiver start");
 /*
        Log.i("AlarmReceiver.java | onReceive", "|" + "============ receive" + "|");

        Bundle extra = intent.getExtras();
        boolean[] week = intent.getBooleanArrayExtra("weekday");
        Log.i("AlarmReceiver.java | onReceive", "|??: " + week[Calendar.SUNDAY]);
        Log.i("AlarmReceiver.java | onReceive", "|??: " + week[Calendar.MONDAY]);
        Log.i("AlarmReceiver.java | onReceive", "|??: " + week[Calendar.TUESDAY]);
        Log.i("AlarmReceiver.java | onReceive", "|??: " + week[Calendar.WEDNESDAY]);
        Log.i("AlarmReceiver.java | onReceive", "|紐?: " + week[Calendar.THURSDAY]);
        Log.i("AlarmReceiver.java | onReceive", "|湲?: " + week[Calendar.FRIDAY]);
        Log.i("AlarmReceiver.java | onReceive", "|??: " + week[Calendar.SATURDAY]);
        Calendar cal = Calendar.getInstance();
        Log.i("AlarmReceiver.java | onReceive", "|" + cal.get(Calendar.DAY_OF_WEEK) + "|");
        // ?ㅻ뒛 ?붿씪???뚮엺??true?대㈃ notification. ?대떦 ?붿씪???꾨땲硫?return ?묒뾽?섏? ?딅뒗??

        if (!week[cal.get(Calendar.DAY_OF_WEEK)])
            return;
*/
        //   myAudioManager.setRingerMode(AudioManager.chg_ring);
        //Log.i(Tag, "set receiver changes RingerMode");
        /*
        String ringer_chg=intent.getStringExtra("changed");
        Log.i(Tag,"ringer mode is : " + ringer_chg);
        */
        Intent i = new Intent(context, ScheduleActivity.class);
        PendingIntent p = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        //if (week[cal.get(Calendar.DAY_OF_WEEK)]) {
        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(android.R.drawable.stat_notify_chat).setTicker("HETT").setWhen(System.currentTimeMillis())
                .setContentTitle("Mode Changed").setContentText("By Schedule")
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(p).setAutoCancel(true);

        notificationmanager.notify(1, builder.build());
        Log.i(Tag, "reset receiver finish");
        //      am.cancel(p);
        // }
        /*
        else if(extra.getBooleanArray("day_of_week") != null)
        {
            //?ㅻ뒛???뚮엺???몃━???붿씪???꾨땲硫?24?쒓컙 ???ㅼ떆 ?뚮엺???앹꽦?쒗궎怨?由ы꽩?⑸땲??
            if (!week[cal.get(Calendar.DAY_OF_WEEK)]) {
                //?뚮엺??24?쒓컙 ?ㅼ뿉 ?몃━???뚯뒪 ?낅젰
                if (calSet.getTimeInMillis() < System.currentTimeMillis())
                    calSet.add(Calendar.DAY_OF_YEAR, 1);
                if (Build.VERSION.SDK_INT >= 23)
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, 24 * 59 * 60 * 1000, p);
                else {
                    if (Build.VERSION.SDK_INT >= 22) {
                        am.setExact(AlarmManager.RTC_WAKEUP, 24 * 59 * 60 * 1000, p);
                    } else {
                        am.set(AlarmManager.RTC_WAKEUP, 24 * 59 * 60 * 1000, p);
                    }
                }
            }
            return;
        }

        */
        //Toast.makeText(context, "Alarm start", Toast.LENGTH_LONG).show(); //3.5珥덇컙

    }
}
