package com.unocode.colormemory;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextClock;
import android.widget.TextView;

/**
 * Created by Chance on 2/19/2017.
 */

public class AboutActivity extends Activity {
    private TextView tvAppVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        tvAppVersion = (TextView) findViewById(R.id.app_version);
        String version = "App Version: " + BuildConfig.VERSION_NAME;
        tvAppVersion.setText(version);
    }
}
