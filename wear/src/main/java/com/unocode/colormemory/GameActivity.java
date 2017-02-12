package com.unocode.colormemory;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Chance on 1/7/2017.
 */

public class GameActivity extends Activity {

    public TextView mTextView, tvWhoseTurn;
    private Integer currentScore;
    private Integer playerIndex;
    public Button btnRed, btnYellow, btnBlue, btnGreen;
    public Activity context;
    public ArrayList<Integer> sequence;
    public final int START_GAME_COUNT = 1;
    SharedPreferences sharedpreferences;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String highscore = "highscore";
    public static final String DifficultySetting = "difficulty";
    public static final String AdaptiveDifficulty = "adaptive_difficulty";
    public int difficultySetAt;
    public boolean adaptiveDifficultySet;
    public int gameSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        sequence = new ArrayList<>();
        currentScore = 1;
        playerIndex = 0;
        context = this;

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        difficultySetAt = sharedpreferences.getInt(DifficultySetting, 0);
        adaptiveDifficultySet = sharedpreferences.getBoolean(AdaptiveDifficulty, false);

        //if adaptivedifficulty, start game at easy
        if(adaptiveDifficultySet)
            gameSpeed = 1000;
        else {
            switch (difficultySetAt) {
                case 0:
                    gameSpeed = 1000;
                    break;
                case 1:
                    gameSpeed = 750;
                    break;
                case 2:
                    gameSpeed = 250;
                    break;
                default:
                    gameSpeed = 1000;
                    break;
            }
        }

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
                tvWhoseTurn = (TextView) stub.findViewById(R.id.tvWhoseTurn);

                Log.d("Game Start", "Game Starting...");
                //save highscore

                Integer tempHighScore = sharedpreferences.getInt("highscore", 1);
                Log.d("Game Start", "Highscore is " + tempHighScore);
                Log.d("Game Start", "Difficulty is " + difficultySetAt);
                GameLoop(START_GAME_COUNT);
            }
        });
    }

    public void toggleAllButtons(boolean enable){ //button class
        if(enable){
            Log.d("gameloop", "enabling buttons");
            displayTurn(false);
        }
        else {
            Log.d("gameloop", "disabling buttons");
            displayTurn(true);
        }

        btnRed.setEnabled(enable);
        btnGreen.setEnabled(enable);
        btnBlue.setEnabled(enable);
        btnYellow.setEnabled(enable);

        btnRed.setClickable(enable);
        btnGreen.setClickable(enable);
        btnBlue.setClickable(enable);
        btnYellow.setClickable(enable);
    }

    public void displayTurn(boolean isCPU){
        if(isCPU)
            tvWhoseTurn.setText(getString(R.string.cpu_turn));
        else
            tvWhoseTurn.setText(getString(R.string.player_turn));
    }

    public void GameLoop(int gameCount) {//game class
        //increase gamespeed every 5 levels
        if(gameCount % 5 == 0 && gameSpeed > 250) {
            gameSpeed -= 50;
            Log.d("gameloop", String.format(Locale.US, "Gamespeed %d", gameSpeed));
        }

        Log.d("gameloop", String.format(Locale.US, "Round: %d", gameCount));
        mTextView.setText(String.format(Locale.US, "%d", gameCount));

        toggleAllButtons(false);//disable buttons
        Random rand = new Random();
        int value = rand.nextInt(4) + 1;//rand between 1-4

        sequence.add(value);

        String sequenceString = "Sequence: ";
        for(int i = 0; i < sequence.size(); ++i) {//print the whole sequence for this round
            sequenceString = sequenceString + sequence.get(i) + ",";
        }

        Log.d("gameloop", sequenceString);
        callSequence(context, sequence);
    }

    public void callSequence(Activity act, ArrayList sequence) {//game class
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
            handler.postDelayed(thread, gameSpeed * (i+1));
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toggleAllButtons(true);//enable buttons
            }
        }, 1000 * sequence.size());
    }

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
                changeBackgroundColor(btn, false);
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

        Log.d("increaseScore", String.format(Locale.US, "Entered: %d. Expected: %d", selectedColor, sequence.get(playerIndex)));

        if(sequence.get(playerIndex) == selectedColor){
            if(playerIndex + 1 >= sequence.size()){//to prevent index out of bounds
                Log.d("increaseScore","Good job!");

                //save highscore
                SharedPreferences.Editor editor = sharedpreferences.edit();
                if(sharedpreferences.getInt("highscore", 0) < currentScore){
                    Log.d("increaseScore", "new highscore is " + currentScore);
                    editor.putInt(highscore, currentScore);
                    editor.apply();

                    Toast toast = Toast.makeText(context, "New highscore!!!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
                    toast.show();
                }

                currentScore++;//
                playerIndex = 0;//reset player index

                GameLoop(currentScore);
            } else {
                playerIndex++;
            }

        } else{
            Log.d("increaseScore", "Failed");

            sequence.clear();//clear the sequence arraylist
            playerIndex = 0;//reset player index
            currentScore = 1;//reset the currentScore

            GameLoop(START_GAME_COUNT);//start game over at 1
        }
    }

    public void changeBackgroundColor(View v, boolean isUserClick){//button class
        //get the button
        final Button localButton = ((Button) v);

        localButton.setPressed(true);

        int countdownTimerNum;
        if(isUserClick) countdownTimerNum = 50;
        else countdownTimerNum = 200;

        new CountDownTimer(countdownTimerNum, countdownTimerNum) {//200ms
            @Override
            public void onTick(long arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onFinish() {
                //change color back to original color
                localButton.setPressed(false);
            }
        }.start();
    }
}