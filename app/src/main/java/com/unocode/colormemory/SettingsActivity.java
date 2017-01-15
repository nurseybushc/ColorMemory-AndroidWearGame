package com.unocode.colormemory;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        switchButton = (Switch) findViewById(R.id.switchButton);
        activity = this;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        switchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (switchButton.isChecked()) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Setting1, "true");
                    editor.apply();

                    Toast.makeText(activity, getResources().getString(R.string.switchOn), Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Setting1, "false");
                    editor.apply();

                    Toast.makeText(activity, getResources().getString(R.string.switchOff), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
