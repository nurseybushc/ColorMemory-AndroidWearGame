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
    private TextView tvMultiplier;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String highscoreSetting = "highscore";
    public static final String DifficultySetting = "difficulty";
    public static final String TimeLimit = "time_limit";
    public static final String Lives = "lives";
    public static final String RandomColors = "random_colors";
    public static final String Randomize = "randomize_list";
    public static final String Reverse = "reverse";
    public static final String AnyOrder = "any_order";
    public static final String DoubleSpeed = "double_speed";
    public static final String Inverse = "inverse";

    public int difficultySetAt;
    public boolean timeLimitSet;
    public boolean livesSet;
    public boolean randomColors;
    public boolean reverseSet;
    public boolean anyOrderSet;
    public boolean randomizeSet;
    public boolean doubleSpeedSet;
    public boolean inverseSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate", "Main activity creating");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        tvHighScore = (TextView) findViewById(R.id.highscore);
        tvHighScore.setText(String.format(Locale.US, "Highscore : %d", sharedpreferences.getInt(highscoreSetting, 1)));

        tvMultiplier = (TextView) findViewById(R.id.multiplier);
        tvMultiplier.setText(String.format(Locale.US, "Multiplier : %d", GetMultiplier()));

    }

    @Override
    public void onResume(){
        Log.d("onResume", "Main activity resuming");
        Log.d("Saved Highscore", "" + sharedpreferences.getInt(highscoreSetting, 1));

        tvHighScore.setText(String.format(Locale.US, "Highscore : %d", sharedpreferences.getInt(highscoreSetting, 1)));
        tvMultiplier.setText(String.format(Locale.US, "Multiplier : %d", GetMultiplier()));

        super.onResume();
    }

    public void startGame(View v)
    {
        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
        startActivity(intent);
    }

    public int GetMultiplier(){
        int scoreMultiplier = 20;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        difficultySetAt = sharedpreferences.getInt(DifficultySetting, 0);
        timeLimitSet = sharedpreferences.getBoolean(TimeLimit, false);
        livesSet = sharedpreferences.getBoolean(Lives, false);
        randomizeSet = sharedpreferences.getBoolean(Randomize, false);
        randomColors = sharedpreferences.getBoolean(RandomColors, false);
        reverseSet = sharedpreferences.getBoolean(Reverse, false);
        anyOrderSet = sharedpreferences.getBoolean(AnyOrder, false);
        doubleSpeedSet = sharedpreferences.getBoolean(DoubleSpeed, false);
        inverseSet = sharedpreferences.getBoolean(Inverse, false);

        switch (difficultySetAt) {
            case 1:
                scoreMultiplier += 2;
                break;
            case 2:
                scoreMultiplier += 5;
                break;
            default:
                break;
        }

        if(randomizeSet) scoreMultiplier += 3;
        if(randomColors) scoreMultiplier += 5;
        if(doubleSpeedSet) scoreMultiplier += 5;
        if(timeLimitSet) scoreMultiplier += 3;
        if(livesSet) scoreMultiplier -= 5;
        if(reverseSet) scoreMultiplier += 8;
        if(anyOrderSet) scoreMultiplier -= 8;
        if(inverseSet) scoreMultiplier += 5;

        return scoreMultiplier;
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
