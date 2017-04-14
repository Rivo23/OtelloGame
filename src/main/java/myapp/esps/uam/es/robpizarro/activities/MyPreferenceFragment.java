package myapp.esps.uam.es.robpizarro.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.content.IntentCompat;

import myapp.esps.uam.es.robpizarro.R;

/**
 * Created by e268930 on 17/03/17.
 */

public class MyPreferenceFragment extends PreferenceFragment implements  SharedPreferences.OnSharedPreferenceChangeListener {


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        PreferenceActivity preferenceActivity =(PreferenceActivity)getActivity();
        if(preferenceActivity!=null) {
            if (key.equals(PreferenceActivity.musicON))
                preferenceActivity.finish();
            else {
                preferenceActivity.finish();
                final Intent intent = getActivity().getIntent();
                getActivity().startActivity(intent);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

}
