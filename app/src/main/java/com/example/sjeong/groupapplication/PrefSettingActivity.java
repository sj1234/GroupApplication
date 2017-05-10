package com.example.sjeong.groupapplication;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;


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
    public static class MyPreferenceFragment extends
            PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_settings);
            Preference pAppName = (Preference) findPreference("setting_activity_id");
            Preference pAppVersion = (Preference) findPreference("setting_activity_app_version");
            CheckBoxPreference cbpAutoAlarm = (CheckBoxPreference) findPreference("setting_activity_autoalarm");
            CheckBoxPreference cbpAlarmReceive = (CheckBoxPreference) findPreference("setting_activity_alarm_reiceive");
            Preference pEmail = (Preference) findPreference("sending_email");

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
        // 자동알림
        else if (preference.getKey().equals("setting_activity_autoalarm")) {
        }
        // 알림 받기
        else if (preference.getKey().equals("setting_activity_alarm_reiceive")) {
        }
        else if (preference.getKey().equals("sending_email")) {
        }

        return false;


    }
}



