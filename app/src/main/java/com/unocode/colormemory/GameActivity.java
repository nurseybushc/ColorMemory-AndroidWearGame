package com.unocode.colormemory;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.RunnableFuture;

import static java.lang.Thread.sleep;

/**
 * Created by Chance on 1/7/2017.
 */

public class GameActivity extends Activity {

    public TextView mTextView;
    private Integer currentScore;
    public Button btnRed, btnYellow, btnBlue, btnGreen;
    public Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        currentScore = 0;
        context = this;

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.tvCurrentScore);
                mTextView.setText(String.format(Locale.US, "%d", currentScore));
                btnRed = (Button) stub.findViewById(R.id.btnRed);
                btnYellow = (Button) stub.findViewById(R.id.btnYellow);
                btnBlue = (Button) stub.findViewById(R.id.btnBlue);
                btnGreen = (Button) stub.findViewById(R.id.btnGreen);

                GameLoop();
            }
        });
    }

    public void GameLoop() {
        int[] gameArray = {1, 2, 3, 4};
        callSequence(context, gameArray);
    }

    public void callSequence(Activity act, int[] sequence) {
        Handler handler = new Handler();
        ClickThread thread;
        for (int i = 0; i < sequence.length; ++i) {
            switch (sequence[i]) {
                case 1:
                    thread = new ClickThread(act, R.id.btnRed);
                    break;
                case 2:
                    thread = new ClickThread(act, R.id.btnGreen);
                    break;
                case 3:
                    thread = new ClickThread(act, R.id.btnBlue);
                    break;
                case 4:
                    thread = new ClickThread(act, R.id.btnYellow);
                    break;
                default:
                    thread = new ClickThread(act, R.id.btnRed);
            }
            handler.postDelayed(thread, 1000 * i);
        }
    }

    //chance try 1/23
    class ClickThread extends Thread {
        private WeakReference<Activity> mActivity;
        private Button btn;

        public ClickThread(Activity activity, int btnId) {
            mActivity = new WeakReference<>(activity);
            btn = (Button) activity.findViewById(btnId);
        }

        @Override
        public void run() {
            Activity activity = mActivity.get();
            if (activity != null) {
                increaseScore(btn);
            }
        }
    }

    public void increaseScore(View v) {
        //get the button
        final Button localButton = ((Button) v);
        //get background color of button
        ColorDrawable buttonColor = (ColorDrawable) localButton.getBackground();
        //get the color id
        final int oldColorId = buttonColor.getColor();

        localButton.setBackgroundColor(Color.WHITE);

        new CountDownTimer(200, 50) {//200ms
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
        mTextView.setText(String.format(Locale.US, "%d", currentScore));
    }
}
