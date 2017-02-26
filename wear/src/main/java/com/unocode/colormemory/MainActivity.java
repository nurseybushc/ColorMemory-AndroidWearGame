package com.unocode.colormemory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends Activity {

    private TextView tvHighScore;
    public TextView tvAppVersion;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String highscoreSetting = "highscore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate", "Main activity creating");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        tvHighScore = (TextView) findViewById(R.id.highscore);
        tvHighScore.setText(String.format(Locale.US, "Highscore : %d", sharedpreferences.getInt(highscoreSetting, 1)));

    }

    @Override
    public void onResume(){
        Log.d("onResume", "Main activity resuming");
        Log.d("Saved Highscore", "" + sharedpreferences.getInt(highscoreSetting, 1));

        tvHighScore.setText(String.format(Locale.US, "Highscore : %d", sharedpreferences.getInt(highscoreSetting, 1)));

        super.onResume();
    }

    public void startGame(View v)
    {
        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
        startActivity(intent);
    }

    public void showSettings(View v)
    {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    public void showAbout(View v)
    {
        Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
        startActivity(intent);
    }
}
