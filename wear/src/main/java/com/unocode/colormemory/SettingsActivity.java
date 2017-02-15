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
    Switch switchAdaptiveDifficulty;
    private Activity activity;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String AdaptiveDifficulty = "adaptive_difficulty";
    public static final String DifficultySetting = "difficulty";
    public static final String highscoreSetting = "highscore";

    Spinner dropdown;
    public int difficultySetAt;
    public boolean adaptiveDifficultySet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        switchAdaptiveDifficulty = (Switch) findViewById(R.id.switchButton);
        dropdown = (Spinner)findViewById(R.id.spinner1);
        activity = this;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        difficultySetAt = sharedpreferences.getInt(DifficultySetting, 0);
        adaptiveDifficultySet = sharedpreferences.getBoolean(AdaptiveDifficulty, false);

        //disable difficulty spinner if adaptive set
        if(adaptiveDifficultySet) dropdown.setEnabled(false);

        switchAdaptiveDifficulty.setChecked(adaptiveDifficultySet);


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
                if(switchAdaptiveDifficulty.isChecked()) dropdown.setEnabled(false);
                else dropdown.setEnabled(true);
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

                    Toast.makeText(activity, "Difficulty is now " + pos, Toast.LENGTH_SHORT).show();
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

        Toast.makeText(activity, "Resetting Highscore", Toast.LENGTH_SHORT).show();
    }
}
