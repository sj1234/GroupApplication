package com.example.sjeong.groupapplication;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    final static String ACTION_CLICK = "com.example.sjeong.groupapplication.CLICK";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object

        // 버튼튼
        SharedPreferences preferences= context.getSharedPreferences("Mode", Activity.MODE_PRIVATE);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        Intent intent = new Intent(context, AppWidget.class);
        intent.setAction(ACTION_CLICK);
        PendingIntent pendindintent = PendingIntent.getBroadcast(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.buttonWidget, pendindintent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // 위젯 업데이트
        if(ACTION_CLICK.equals(intent.getAction())){

            //버튼 화면!!!
            Toast.makeText(context,"mode off", Toast.LENGTH_LONG).show();

            SharedPreferences preferences= context.getSharedPreferences("Mode", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("set", "off");
            editor.commit();

            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            this.onUpdate(context, manager, manager.getAppWidgetIds(new ComponentName(context, AppWidget.class)));
            return;
        }
		else
                super.onReceive(context, intent);

    }

    /*
    onUpdate()는 위젯 갱신 주기에 따라 위젯을 갱신할때 호출됩니다
    onEnabled()는 위젯이 처음 생성될때 호출되며, 동일한 위젯의 경우 처음 호출됩니다
    onDisabled()는 위젯의 마지막 인스턴스가 제거될때 호출됩니다
    onDeleted()는 위젯이 사용자에 의해 제거될때 호출됩니다
    */
}

