package com.unocode.colormemory;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.content.Context;


public class SettingsActivity extends Activity {
    Switch switchRandomize, switchTimeLimit, switchLives, switchRandomColors, switchReverse, switchDoubleSpeed, switchInverse;
    private Activity activity;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String TimeLimit = "time_limit";
    public static final String Lives = "lives";
    public static final String RandomColors = "random_colors";
    public static final String Reverse = "reverse";
    public static final String DoubleSpeed = "double_speed";
    public static final String Inverse = "inverse";

    public static final String Randomize = "randomize_list";
    public static final String DifficultySetting = "difficulty";
    public static final String highscoreSetting = "highscore";


    Spinner dropdown;
    public int difficultySetAt;
    public boolean timeLimitSet;
    public boolean reverseSet;
    public boolean livesSet;
    public boolean randomizeList;
    public boolean randomColors;
    public boolean doubleSpeedSet;
    public boolean inverseSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dropdown = (Spinner)findViewById(R.id.spinner1);
        switchRandomize = (Switch) findViewById(R.id.switchRandomize);
        switchTimeLimit = (Switch) findViewById(R.id.switchTimeLimit);
        switchLives = (Switch) findViewById(R.id.switchLives);
        switchReverse = (Switch) findViewById(R.id.switchReverse);
        switchRandomColors = (Switch)findViewById(R.id.switchRandomColors);
        switchDoubleSpeed = (Switch) findViewById(R.id.switchDoubleSpeed);
        switchInverse = (Switch) findViewById(R.id.switchInverse);

        activity = this;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        difficultySetAt = sharedpreferences.getInt(DifficultySetting, 0);
        reverseSet = sharedpreferences.getBoolean(Reverse, false);
        doubleSpeedSet = sharedpreferences.getBoolean(DoubleSpeed, false);
        timeLimitSet = sharedpreferences.getBoolean(TimeLimit, false);
        livesSet = sharedpreferences.getBoolean(Lives, false);
        randomizeList = sharedpreferences.getBoolean(Randomize, false);
        randomColors = sharedpreferences.getBoolean(RandomColors, false);
        inverseSet = sharedpreferences.getBoolean(Inverse, false);


        switchTimeLimit.setChecked(timeLimitSet);
        switchReverse.setChecked(reverseSet);
        switchRandomize.setChecked(randomizeList);
        switchLives.setChecked(livesSet);
        switchRandomColors.setChecked(randomColors);
        switchDoubleSpeed.setChecked(doubleSpeedSet);
        switchInverse.setChecked(inverseSet);

        Resources res = getResources();
        dropdown.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, res.getStringArray(R.array.difficulty)));

        dropdown.setSelection(difficultySetAt);

        switchRandomize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                boolean switchRandomizeChecked = switchRandomize.isChecked();
                String incDecMult = switchRandomizeChecked ? "+3" : "-3";
                Toast.makeText(activity, "Multiplier " + incDecMult,Toast.LENGTH_SHORT).show();
                editor.putBoolean(Randomize, switchRandomizeChecked);
                editor.apply();
            }
        });

        switchTimeLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                boolean switchTimeLimitChecked = switchTimeLimit.isChecked();
                String incDecMult = switchTimeLimitChecked ? "+3" : "-3";
                Toast.makeText(activity, "Multiplier " + incDecMult,Toast.LENGTH_SHORT).show();
                editor.putBoolean(TimeLimit, switchTimeLimitChecked);
                editor.apply();
            }
        });

        switchLives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                boolean switchLivesChecked = switchLives.isChecked();
                String incDecMult = switchLivesChecked ? "-3" : "+3";
                Toast.makeText(activity, "Multiplier " + incDecMult,Toast.LENGTH_SHORT).show();
                editor.putBoolean(Lives, switchLivesChecked);
                editor.apply();
            }
        });

        switchRandomColors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                boolean switchRandomColorsChecked = switchRandomColors.isChecked();
                String incDecMult = switchRandomColorsChecked ? "+5" : "-5";
                Toast.makeText(activity, "Multiplier " + incDecMult,Toast.LENGTH_SHORT).show();
                editor.putBoolean(RandomColors, switchRandomColorsChecked);
                editor.apply();
            }
        });

        switchReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                boolean switchReverseChecked = switchReverse.isChecked();
                String incDecMult = switchReverseChecked ? "+8" : "-8";
                Toast.makeText(activity, "Multiplier " + incDecMult,Toast.LENGTH_SHORT).show();
                editor.putBoolean(Reverse, switchReverseChecked);
                editor.apply();
            }
        });

        switchDoubleSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                boolean switchDoubleSpeedChecked = switchDoubleSpeed.isChecked();
                String incDecMult = switchDoubleSpeedChecked ? "+5" : "-5";
                Toast.makeText(activity, "Multiplier " + incDecMult,Toast.LENGTH_SHORT).show();
                editor.putBoolean(DoubleSpeed, switchDoubleSpeedChecked);
                editor.apply();
            }
        });
        switchInverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                boolean switchInverseChecked = switchInverse.isChecked();
                String incDecMult = switchInverseChecked ? "+5" : "-5";
                Toast.makeText(activity, "Multiplier " + incDecMult,Toast.LENGTH_SHORT).show();
                editor.putBoolean(Inverse, switchInverseChecked);
                editor.apply();
            }
        });

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                if(pos != difficultySetAt) {//difficulty is NOT already set at clicked value

                    difficultySetAt = pos;
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putInt(DifficultySetting, pos);
                    editor.apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

    }
    public void ResetScore(View v){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(highscoreSetting, 1);
        editor.apply();
    }

    public void SetDefaults(View v){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        switchTimeLimit.setChecked(false);
        switchReverse.setChecked(false);
        switchRandomize.setChecked(false);
        switchLives.setChecked(false);
        switchRandomColors.setChecked(false);
        switchDoubleSpeed.setChecked(false);
        switchInverse.setChecked(false);
        dropdown.setSelection(0);

        editor.putBoolean(TimeLimit, false);
        editor.putBoolean(Reverse, false);
        editor.putBoolean(Randomize, false);
        editor.putBoolean(Lives, false);
        editor.putBoolean(RandomColors, false);
        editor.putBoolean(DoubleSpeed, false);
        editor.putBoolean(Inverse, false);
        editor.putInt(DifficultySetting, 0);

        editor.apply();
    }
}
