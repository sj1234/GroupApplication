package com.example.sjeong.groupapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import static android.R.attr.defaultValue;
import static android.R.attr.switchTextOff;
import static android.R.attr.switchTextOn;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


public class PrefSettingActivity extends PreferenceActivity implements OnPreferenceClickListener

{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content,
                        new MyPreferenceFragment()).commit();



    }

    // PreferenceFragment 클래스 사용
    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_settings);
            Preference pAppName = (Preference) findPreference("setting_activity_id");
            Preference pAppVersion = (Preference) findPreference("setting_activity_app_version");
            CheckBoxPreference cbpAlarmReceive = (CheckBoxPreference) findPreference("setting_activity_alarm_reiceive");
            Preference pEmail = (Preference) findPreference("sending_email");
            final SwitchPreference pPushlater =  (SwitchPreference) findPreference("push_later");
            Preference pBack = (Preference) findPreference("back");


            pAppName.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Toast.makeText(getActivity(), "Alarm Call", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            pAppVersion.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Toast.makeText(getActivity(), "Beta", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

           pEmail.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Toast.makeText(getActivity(), "Sending Email", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            pBack.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    return false;
                }
            });



            /*SharedPreferences preferences = getSharedPreferences("Later", Activity.MODE_PRIVATE);
            pPushlater.setChecked(preferences.getString("onoff", "off").equals("on"));
            pPushlater.setText("나중에 알림");
            */

            PreferenceManager preferenceManager = getPreferenceManager();
            preferenceManager.setSharedPreferencesName("Later");
            preferenceManager.setSharedPreferencesMode(Activity.MODE_PRIVATE);

            pPushlater.setChecked(preferenceManager.getSharedPreferences().getString("onoff", "off").equals("on"));


            pPushlater.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                   /* SharedPreferences preferences = getSharedPreferences("Later", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    if (isChecked == true){
                        editor.putString("onoff", "on");
                    } else {
                        editor.putString("onoff", "off");
                    }
                    editor.commit();
                    */
                    PreferenceManager preferenceManager = getPreferenceManager();
                    preferenceManager.setSharedPreferencesName("Later");
                    preferenceManager.setSharedPreferencesMode(Activity.MODE_PRIVATE);
                    SharedPreferences preferences = getPreferenceManager().getSharedPreferences();
                    SharedPreferences.Editor editor = preferences.edit();
                    if (pPushlater.isChecked()==true){
                        editor.putString("onoff", "on");
                    } else {
                        editor.putString("onoff", "off");
                    }
                    editor.commit();

                    return false;
                }
            });

        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        // 어플리케이션 이름
        if (preference.getKey().equals("setting_activity_id")) {
        }
        // 어플리케이션 버전
        else if (preference.getKey().equals("setting_activity_app_version")) {
        }
        // 알림 받기
        else if (preference.getKey().equals("setting_activity_alarm_reiceive")) {
        }
        else if (preference.getKey().equals("sending_email")) {
        }
        else if (preference.getKey().equals("push_later")) {
        }
        else if (preference.getKey().equals("back")) {
        }
        return false;


    }



}



