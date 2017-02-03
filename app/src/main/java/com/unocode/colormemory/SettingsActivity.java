package com.unocode.colormemory;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.content.Context;


/**
 * Created by Chance on 1/9/2017.
 */

public class SettingsActivity extends Activity {
    Switch switchButton;
    private Activity activity;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Setting1 = "setting1key";
    public static final String DifficultySetting = "difficulty";
    public static final String highscoreSetting = "highscore";

    Spinner dropdown;
    public int difficultySetAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        switchButton = (Switch) findViewById(R.id.switchButton);
        dropdown = (Spinner)findViewById(R.id.spinner1);
        activity = this;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        difficultySetAt = sharedpreferences.getInt(DifficultySetting, 0);


        Resources res = getResources();
        dropdown.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, res.getStringArray(R.array.difficulty)));

        dropdown.setSelection(difficultySetAt);

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(Setting1, switchButton.isChecked());
                editor.apply();

                String tempText = switchButton.isChecked()? getResources().getString(R.string.switchOn) : getResources().getString(R.string.switchOff);
                Toast.makeText(activity, tempText, Toast.LENGTH_SHORT).show();
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
