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
    Switch switchAdaptiveDifficulty, switchRandomize, switchTimeLimit, switchLives, switchRandomColors;
    private Activity activity;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String AdaptiveDifficulty = "adaptive_difficulty";
    public static final String TimeLimit = "time_limit";
    public static final String Lives = "lives";
    public static final String RandomColors = "random_colors";


    public static final String Randomize = "randomize_list";
    public static final String DifficultySetting = "difficulty";
    public static final String highscoreSetting = "highscore";

    Spinner dropdown;
    public int difficultySetAt;
    public boolean adaptiveDifficultySet;
    public boolean timeLimitSet;
    public boolean livesSet;
    public boolean randomizeList;
    public boolean randomColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        switchAdaptiveDifficulty = (Switch) findViewById(R.id.switchAdaptive);
        dropdown = (Spinner)findViewById(R.id.spinner1);
        switchRandomize = (Switch) findViewById(R.id.switchRandomize);
        switchTimeLimit = (Switch) findViewById(R.id.switchTimeLimit);
        switchLives = (Switch) findViewById(R.id.switchLives);
        switchRandomColors = (Switch)findViewById(R.id.switchRandomColors);

        activity = this;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        difficultySetAt = sharedpreferences.getInt(DifficultySetting, 0);
        adaptiveDifficultySet = sharedpreferences.getBoolean(AdaptiveDifficulty, false);
        timeLimitSet = sharedpreferences.getBoolean(TimeLimit, false);
        livesSet = sharedpreferences.getBoolean(Lives, false);
        randomizeList = sharedpreferences.getBoolean(Randomize, false);
        randomColors = sharedpreferences.getBoolean(RandomColors, false);

        //disable difficulty spinner if adaptive set
        if(adaptiveDifficultySet) {
            dropdown.setEnabled(false);
            switchRandomize.setEnabled(false);
            switchTimeLimit.setEnabled(false);
            switchRandomColors.setEnabled(false);
        }

        switchAdaptiveDifficulty.setChecked(adaptiveDifficultySet);
        switchTimeLimit.setChecked(timeLimitSet);
        switchRandomize.setChecked(randomizeList);
        switchLives.setChecked(livesSet);
        switchRandomColors.setChecked(randomColors);

        Resources res = getResources();
        dropdown.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, res.getStringArray(R.array.difficulty)));

        dropdown.setSelection(difficultySetAt);

        switchAdaptiveDifficulty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(AdaptiveDifficulty, switchAdaptiveDifficulty.isChecked());
                editor.apply();

                //disable difficulty spinner if adaptive set
                if(switchAdaptiveDifficulty.isChecked()){
                    dropdown.setEnabled(false);
                    switchRandomize.setEnabled(false);
                    switchTimeLimit.setEnabled(false);
                    switchRandomColors.setEnabled(false);
                }
                else {
                    dropdown.setEnabled(true);
                    switchRandomize.setEnabled(true);
                    switchTimeLimit.setEnabled(true);
                    switchRandomColors.setEnabled(true);
                }
            }
        });

        switchRandomize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(Randomize, switchRandomize.isChecked());
                editor.apply();
            }
        });

        switchTimeLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(TimeLimit, switchTimeLimit.isChecked());
                editor.apply();
            }
        });

        switchLives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(Lives, switchLives.isChecked());
                editor.apply();
            }
        });

        switchRandomColors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(RandomColors, switchRandomColors.isChecked());
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
}
