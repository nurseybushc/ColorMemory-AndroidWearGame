package com.unocode.colormemory;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by Chance on 1/7/2017.
 */

public class GameActivity extends Activity {

    public TextView mTextView;
    private Integer currentScore;
    public Button btnRed, btnYellow, btnBlue, btnGreen;

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
                btnRed = (Button) stub.findViewById(R.id.btnRed);
                btnYellow = (Button) stub.findViewById(R.id.btnYellow);
                btnBlue = (Button) stub.findViewById(R.id.btnBlue);
                btnGreen = (Button) stub.findViewById(R.id.btnGreen);
            }
        });
    }

    public void increaseScore(View v)
    {
        //get the button
        final Button localButton = ((Button)v);
        //get background color of button
        ColorDrawable buttonColor = (ColorDrawable) localButton.getBackground();
        //get the color id
        final int oldColorId = buttonColor.getColor();

        localButton.setBackgroundColor(Color.WHITE);

        new CountDownTimer(200, 50) {//100ms
            @Override
            public void onTick(long arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onFinish() {
                //change color back to original color
                localButton.setBackgroundColor(oldColorId);
            }
        }.start();
        currentScore += 1;
        mTextView.setText(String.format(Locale.US,"%d",currentScore));
    }
}
