package com.unocode.colormemory;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by Chance on 1/7/2017.
 */

public class GameActivity extends Activity {

    public TextView mTextView;
    private Integer currentScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        currentScore = 0;

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.tvCurrentScore);
                mTextView.setText(String.format(Locale.US,"%d",currentScore));
            }
        });
    }

    public void increaseScore(View v)
    {
        currentScore += 1;
        mTextView.setText(String.format(Locale.US,"%d",currentScore));
    }
}
