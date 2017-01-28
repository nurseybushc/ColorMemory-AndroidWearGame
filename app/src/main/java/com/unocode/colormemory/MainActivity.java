package com.unocode.colormemory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends Activity {

    private TextView tvGameTitle, tvHighScore;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String highscore = "highscore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                tvGameTitle = (TextView) stub.findViewById(R.id.gametitle);
                tvHighScore = (TextView) stub.findViewById(R.id.highscore);
                tvHighScore.setText(String.format(Locale.US, "Highscore : %d", sharedpreferences.getInt(highscore, 0)));
            }
        });

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
}
