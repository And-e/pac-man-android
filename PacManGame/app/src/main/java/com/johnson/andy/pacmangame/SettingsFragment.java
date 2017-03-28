package com.johnson.andy.pacmangame;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment
            implements SharedPreferences.OnSharedPreferenceChangeListener {


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences prefs = getPreferenceScreen().getSharedPreferences();
        onSharedPreferenceChanged(prefs, "additional_ghosts");
        onSharedPreferenceChanged(prefs, "number_ghosts");
    }

    @Override
    public void onResume()
    {
        super.onResume();

        getPreferenceScreen().getSharedPreferences().
                registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();

        getPreferenceScreen().getSharedPreferences().
                unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Preference pref = findPreference(key);

        switch(key)
        {

            case "number_ghosts" :
                pref.setSummary(sharedPreferences.getString(key, "2"));
                break;

            case "additional_ghosts" :
                pref.setSummary(sharedPreferences.getString(key, "2"));
                break;

            default :
                pref.setSummary(sharedPreferences.getString(key,""));
        }
    }
}
