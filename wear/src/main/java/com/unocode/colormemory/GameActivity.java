package com.unocode.colormemory;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GameActivity extends Activity {

    public TextView mTextView, tvWhoseTurn, tvScore, tvLives, tvTimeLimit;
    private Integer currentCount;
    private Integer currentScore;
    private Integer playerIndex;
    public Button btnRed, btnYellow, btnBlue, btnGreen;
    public Activity context;
    public ArrayList<Integer> sequence;
    public final int START_GAME_COUNT = 1;
    SharedPreferences sharedpreferences;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String highscore = "highscore";
    public static final String DifficultySetting = "difficulty";
    public static final String AdaptiveDifficulty = "adaptive_difficulty";
    public static final String TimeLimit = "time_limit";
    public static final String Lives = "lives";
    public static final String RandomColors = "random_colors";
    public static final String Randomize = "randomize_list";

    public int difficultySetAt;
    public boolean adaptiveDifficultySet;
    public boolean timeLimitSet;
    public boolean livesSet;
    public boolean failed;
    public boolean randomColors;

    public int timeLimit;
    public int livesCount, currentLives;
    public CountDownTimer cdt;
    public boolean randomizeSet;
    public int gameSpeed;

    //score related
    public int scoreMultipler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        sequence = new ArrayList<>();
        currentCount = START_GAME_COUNT;
        currentScore = currentCount;
        playerIndex = 0;
        context = this;

        //default scoreMultipler = 1;
        scoreMultipler = 10;

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        difficultySetAt = sharedpreferences.getInt(DifficultySetting, 0);
        adaptiveDifficultySet = sharedpreferences.getBoolean(AdaptiveDifficulty, false);
        timeLimitSet = sharedpreferences.getBoolean(TimeLimit, false);
        livesSet = sharedpreferences.getBoolean(Lives, false);
        failed = false;
        randomizeSet = sharedpreferences.getBoolean(Randomize, false);
        randomColors = sharedpreferences.getBoolean(RandomColors, false);

        //if adaptivedifficulty, start game at easy and dont randomize
        if (adaptiveDifficultySet) {
            gameSpeed = 1000;
            randomizeSet = false;
        } else {
            switch (difficultySetAt) {
                case 0:
                    gameSpeed = 1000;
                    timeLimit = 4000;
                    livesCount = 5;
                    break;
                case 1:
                    gameSpeed = 750;
                    timeLimit = 3000;
                    scoreMultipler += 2;
                    livesCount = 3;
                    break;
                case 2:
                    gameSpeed = 350;
                    timeLimit = 2000;
                    scoreMultipler += 5;
                    livesCount = 1;
                    break;
                default:
                    gameSpeed = 1000;
                    timeLimit = 4000;
                    livesCount = 5;
                    break;
            }
        }

        currentLives = livesCount;
        if (randomizeSet) scoreMultipler += 3;

        setContentView(R.layout.activity_game);
        mTextView = (TextView) findViewById(R.id.tvCurrentScore);
        mTextView.setText(String.format(Locale.US, "%d", currentCount));

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameLoop(currentCount);//start game over at 1
            }
        });

        tvTimeLimit = (TextView) findViewById(R.id.tvTimeLimit);
        if(timeLimitSet) {
            //display time left textview toward bottom
        }
        tvLives = (TextView) findViewById(R.id.tvLives);
        if(livesSet){
            tvLives.setText(String.format(Locale.US, "%d", currentLives));
        }

        tvScore = (TextView) findViewById(R.id.tvScore);
        tvScore.setText(String.format(Locale.US, "%d", currentScore));

        btnRed = (Button) findViewById(R.id.btnRed);
        btnYellow = (Button) findViewById(R.id.btnYellow);
        btnBlue = (Button) findViewById(R.id.btnBlue);
        btnGreen = (Button) findViewById(R.id.btnGreen);
        tvWhoseTurn = (TextView) findViewById(R.id.tvWhoseTurn);

        Log.d("Game Start", "Game Starting...");
        //save highscore

        Integer tempHighScore = sharedpreferences.getInt("highscore", 1);
        Log.d("Game Start", "Highscore:" + tempHighScore);
        Log.d("Game Start", "Difficulty:" + difficultySetAt);
        Log.v("Game Start", "Adaptive Difficulty Set: " + adaptiveDifficultySet);
        Log.v("Game Start", "Randomize Lists Set: " + randomizeSet);

        GameLoop(currentCount);
    }

    public void toggleAllButtons(boolean enable) { //button class
        if (enable) {
            Log.d("gameloop", "enabling buttons");
            displayTurn(false);
        } else {
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

    public void displayTurn(boolean isCPU) {
        if (isCPU)
            tvWhoseTurn.setText(getString(R.string.cpu_turn));
        else
            tvWhoseTurn.setText(getString(R.string.player_turn));
    }

    public void GameLoop(int gameCount) {//game class
        //increase gamespeed every 5 levels
        if (adaptiveDifficultySet && (gameCount % 5 == 0) && (gameSpeed > 350)) {
            gameSpeed -= 50;
            Log.d("gameloop", String.format(Locale.US, "Gamespeed %d", gameSpeed));

            //randomly randomize list
            randomizeSet = new Random().nextBoolean();
            Log.v("gameloop", "Randomize Lists: " + randomizeSet);
        }

        Log.d("gameloop", String.format(Locale.US, "Round: %d", gameCount));

        mTextView.setClickable(false);
        mTextView.setText(String.format(Locale.US, "%d", gameCount));


        toggleAllButtons(false);//disable buttons
        Random rand = new Random();
        int value = rand.nextInt(4) + 1;//rand between 1-4

        if (livesSet && failed) {
            Log.d("gameloop", "else liveset is true and previously failed");
            failed = false;
        } else if (!randomizeSet) {
            Log.d("gameloop", "if randomize and liveset are false");
            sequence.add(value);
        } else {
            Log.d("gameloop", "else randomize is true and liveset is false");
            sequence.clear();//clear list
            for (int i = 0; i < gameCount; ++i) {
                sequence.add(value);
                value = rand.nextInt(4) + 1;//rand between 1-4
            }
        }

        String sequenceString = "Sequence: ";
        for (int i = 0; i < sequence.size(); ++i) {//print the whole sequence for this round
            sequenceString = sequenceString + sequence.get(i) + ",";
        }

        if (randomColors) randomizeButtonColors();
        Log.d("gameloop", sequenceString);
        callSequence(context, sequence);
    }

    public void randomizeButtonColors() {
        //1-red,2-green,3-blue,4-yellow

        ArrayList<Integer> buttonIndexes = new ArrayList<>();
        buttonIndexes.add(0);
        buttonIndexes.add(1);
        buttonIndexes.add(2);
        buttonIndexes.add(3);

        Collections.shuffle(buttonIndexes);
        //maybe button array or something to hold the buttons
        int firstButtonIndex = buttonIndexes.remove(0);
        int secondButtonIndex = buttonIndexes.remove(0);
        int thirdButtonIndex = buttonIndexes.remove(0);
        int fourthButtonIndex = buttonIndexes.remove(0);

        Drawable[] drawables = {btnRed.getBackground(), btnGreen.getBackground(), btnBlue.getBackground(), btnYellow.getBackground()};

        btnRed.setBackground(drawables[firstButtonIndex]);
        btnGreen.setBackground(drawables[secondButtonIndex]);
        btnBlue.setBackground(drawables[thirdButtonIndex]);
        btnYellow.setBackground(drawables[fourthButtonIndex]);
    }

    public void callSequence(Activity act, final ArrayList sequence) {//game class
        Handler handler = new Handler();
        ClickThread thread;

        for (int i = 0; i < sequence.size(); ++i) {
            switch ((int) sequence.get(i)) {
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
            if (sequence.size() == 1)
                handler.postDelayed(thread, 1000 * (i + 1)); //wait a bit longer on the first round
            else handler.postDelayed(thread, gameSpeed * (i + 1));
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toggleAllButtons(true);//enable buttons
                if (timeLimitSet) {
                    cdt = new CountDownTimer(timeLimit * sequence.size(), 1) {
                        public void onTick(long millisUntilFinished) {
                            tvTimeLimit.setText(String.format(Locale.US,"%d.%d", TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished),millisUntilFinished));
                        }

                        public void onFinish() {
                            showFailedMessage();
                        }
                    }.start();
                }
            }
        }, gameSpeed * sequence.size());

    }

    class ClickThread extends Thread {
        private WeakReference<Activity> mActivity;
        private Button btn;

        ClickThread(Activity activity, int btnId) {
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

    public int calculateScore() {
        return currentCount * scoreMultipler;
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

        if (sequence.get(playerIndex) == selectedColor) {
            if (playerIndex + 1 >= sequence.size()) {//to prevent index out of bounds
                if (cdt != null) cdt.cancel();
                Log.d("increaseScore", "Good job!");

                currentScore = calculateScore();
                tvScore.setText(String.format(Locale.US, "%d", currentScore));

                //save highscore
                SharedPreferences.Editor editor = sharedpreferences.edit();
                if (sharedpreferences.getInt("highscore", 0) < currentScore) {
                    Log.d("increaseScore", "new highscore is " + currentScore);

                    editor.putInt(highscore, currentScore);
                    editor.apply();
                }

                currentCount++;//
                playerIndex = 0;//reset player index

                GameLoop(currentCount);
            } else {
                playerIndex++;
            }

        } else if (livesSet && currentLives > 0) {//fail but with lives
            Log.d("increaseScore", "Failed but with lives");
            failed = true;
            currentLives--;
            tvLives.setText(String.format(Locale.US, "%d", currentLives));
            if (cdt != null) cdt.cancel();
            playerIndex = 0;//reset player index
            showContinueMessage();
        } else {
            Log.d("increaseScore", "Failed");
            if (cdt != null) cdt.cancel();
            showFailedMessage();
        }
    }

    public void showFailedMessage() {
        sequence.clear();//clear the sequence arraylist
        playerIndex = 0;//reset player index
        currentCount = 1;//reset the currentCount
        currentScore = currentCount;
        currentLives = livesCount;

        tvScore.setText(String.format(Locale.US, "%d", currentScore));
        tvLives.setText(String.format(Locale.US, "%d", currentLives));

        toggleAllButtons(false);
        mTextView.setText(getResources().getString(R.string.failedMessage));
        mTextView.setClickable(true);

    }

    public void showContinueMessage() {
        toggleAllButtons(false);
        mTextView.setText(getResources().getString(R.string.continueGame));
        mTextView.setClickable(true);
    }


    public void changeBackgroundColor(View v, boolean isUserClick) {//button class
        //get the button
        final Button localButton = ((Button) v);

        localButton.setPressed(true);

        int countdownTimerNum;
        if (isUserClick) countdownTimerNum = 50;
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
