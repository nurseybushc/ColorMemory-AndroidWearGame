package com.unocode.colormemory;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Chance on 1/7/2017.
 */

public class GameActivity extends Activity {

    public TextView mTextView;
    private Integer currentScore;
    private Integer playerIndex;
    public Button btnRed, btnYellow, btnBlue, btnGreen;
    public Activity context;
    public ArrayList<Integer> sequence;
    public final int START_GAME_COUNT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        sequence = new ArrayList<>();
        currentScore = 1;
        playerIndex = 0;
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

                try
                {
                    Thread.sleep(1000);//sleep the thread before changing color
                }
                catch(Exception ex){

                }

                Log.d("Game Start", "Game Starting...");
                GameLoop(START_GAME_COUNT);
            }
        });
    }

    public void GameLoop(int gameCount) {
        Log.d("gameloop", String.format(Locale.US, "Round: %d", gameCount));
        mTextView.setText(String.format(Locale.US, "%d", gameCount));

        Random rand = new Random();
        int value = rand.nextInt(4) + 1;//rand between 1-4

        sequence.add(value);

        for(int i = 0; i < sequence.size(); ++i) {//print the whole sequence for this round
            Log.d("gameloop", String.format(Locale.US, "%d", sequence.get(i)));
        }

        callSequence(context, sequence);
    }

    public void callSequence(Activity act, ArrayList sequence) {
        Handler handler = new Handler();
        ClickThread thread;
        for (int i = 0; i < sequence.size(); ++i) {
            switch ((int)sequence.get(i)) {
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
                changeBackgroundColor(btn);
            }
        }
    }

    public void increaseScore(View v) {

        int selectedColor = 0;
        switch (v.getId()) {
            case (R.id.btnRed):
                selectedColor = 1;
                break;
            case (R.id.btnGreen):
                selectedColor = 2;
                break;
            case (R.id.btnBlue):
                selectedColor = 3;
                break;
            case (R.id.btnYellow):
                selectedColor = 4;
                break;
        }

        Log.d("increaseScore", String.format(Locale.US, "Entered: %d", selectedColor));
        Log.d("increaseScore", String.format(Locale.US, "Expected: %d", sequence.get(playerIndex)));

        if(sequence.get(playerIndex) == selectedColor){
            if(playerIndex + 1 >= sequence.size()){//to prevent index out of bounds
                Log.d("increaseScore","Good job!");

                currentScore++;//
                playerIndex = 0;//reset player index

                try{
                    Thread.sleep(1000);//sleep the thread before changing color
                }catch(Exception ex){

                }
                GameLoop(currentScore);
            } else {
                playerIndex++;
            }

        } else{
            Log.d("increaseScore", "Failed");

            sequence.clear();//clear the sequence arraylist
            playerIndex = 0;//reset player index
            currentScore = 1;//reset the currentScore
            try{
                Thread.sleep(2000);//sleep the thread before changing color
            }catch(Exception ex){

            }
            GameLoop(START_GAME_COUNT);//start game over at 1
        }
    }

    public void changeBackgroundColor(View v){
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
    }
}
