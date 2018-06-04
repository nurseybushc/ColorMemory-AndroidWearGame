package com.unocode.colormemory;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextClock;
import android.widget.TextView;

/**
 * Created by Chance on 2/19/2017.
 */

public class AboutActivity extends Activity {
    private TextView tvAppVersion, tvHighscore, tvHighcount, tvTotalGamesPlayed;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String highscoreSetting = "highscore";
    public static final String highcountSetting = "highcount";
    public static final String totalGamesPlayed = "total_games_played";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        tvAppVersion = (TextView) findViewById(R.id.app_version);
        String version = "App Version: " + BuildConfig.VERSION_NAME;
        tvAppVersion.setText(version);

        tvHighscore = (TextView) findViewById(R.id.highscore);
        String highscoreText = "Highscore: " + sharedpreferences.getInt(highscoreSetting, 1);
        tvHighscore.setText(highscoreText);

        tvHighcount = (TextView) findViewById(R.id.highcount);
        String highcountText = "Highcount: " + sharedpreferences.getInt(highcountSetting, 1);
        tvHighcount.setText(highcountText);

        tvTotalGamesPlayed = (TextView) findViewById(R.id.totalGamesPlayed);
        String totalGamesPlayedText = "Games Played: " + sharedpreferences.getInt(totalGamesPlayed, 0);
        tvTotalGamesPlayed.setText(totalGamesPlayedText);

    }
}
