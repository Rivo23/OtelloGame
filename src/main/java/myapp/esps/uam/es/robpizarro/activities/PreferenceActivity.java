package myapp.esps.uam.es.robpizarro.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.view.KeyEvent;

import myapp.esps.uam.es.robpizarro.R;
import myapp.esps.uam.es.robpizarro.models.MusicManager;

/**
 * Created by e268930 on 17/03/17.
 */

public class PreferenceActivity extends BaseActivity {
    public final static String backStyle = "style";
    public final static String defStyle = "android";
    public final static String playerUUID = "playerUUID";
    public final static String defUUID = "999999";
    public final static String playerName = "playerName";
    public final static String defName = "Humano";
    public final static String playerPass = "playerPass";
    public final static String defPass = "1234";
    public final static String musicON = "music";
    public final static boolean isMusic = true;
    public final static String firstTurn = "turn";
    public final static boolean isfirst = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        continueMusic=true;
        setContentView(R.layout.preference_layout);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        MyPreferenceFragment fragment = new MyPreferenceFragment();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(fragment);
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }


    public static String getTheme(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(backStyle, defStyle);
    }

    public static void setMusicON(Context context, boolean ok){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PreferenceActivity.musicON, ok);
        editor.commit();
    }
    public static boolean getMusicON(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(musicON, isMusic);
    }

    public static void setPlayerUUID(Context context, String s){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PreferenceActivity.playerUUID, s);
        editor.commit();
    }
    public static String getUUIDPlayer(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(playerUUID, defUUID);
    }
    public static void deletePlayerUUIDPreference(Context context){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(PreferenceActivity.playerUUID);
        editor.apply();
    }

    public static void setPlayerName(Context context, String s){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PreferenceActivity.playerName, s);
        editor.commit();
    }
    public static String getPlayerName(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(playerName, defName);
    }

    public static void deletePlayerNamePreference(Context context){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(PreferenceActivity.playerName);
        editor.apply();
    }

    public static void setPassword(Context context, String pass) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PreferenceActivity.playerPass, pass);
        editor.commit();
    }
    public static String getPassword(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(playerPass, defPass);
    }
    public static void deletePasswordPreference(Context context){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(PreferenceActivity.playerPass);
        editor.apply();
    }

    public static void setTurno(Context context, boolean ok) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PreferenceActivity.firstTurn, ok);
        editor.commit();
    }
    public static boolean getTurno(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(firstTurn, isfirst);
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent){
        if(keyEvent.getKeyCode()==KeyEvent.KEYCODE_BACK){
            continueMusic=true;
        }
        return continueMusic;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!continueMusic) {
            MusicManager.pause();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        continueMusic = false;
        if (PreferenceActivity.getMusicON(this)) {
            MusicManager.start(this, MusicManager.MUSIC_MENU);
        }
    }
}
