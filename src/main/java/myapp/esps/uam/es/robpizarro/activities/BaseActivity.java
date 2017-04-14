package myapp.esps.uam.es.robpizarro.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import myapp.esps.uam.es.robpizarro.R;

/**
 * Created by e268930 on 29/03/17.
 */

public class BaseActivity extends AppCompatActivity {
    protected boolean continueMusic=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (PreferenceActivity.getTheme(this).equals(getResources().getString(R.string.olitheme))) {
            setTheme(R.style.OliveTheme);
        }else if (PreferenceActivity.getTheme(this).equals(getResources().getString(R.string.marinetheme))){
            setTheme(R.style.MarineTheme);
        }else if (PreferenceActivity.getTheme(this).equals(getResources().getString(R.string.foresttheme))){
            setTheme(R.style.ForestTheme);
        }else if (PreferenceActivity.getTheme(this).equals(getResources().getString(R.string.oasistheme))){
            setTheme(R.style.OasisTheme);
        }
        super.onCreate(savedInstanceState);
    }

}
