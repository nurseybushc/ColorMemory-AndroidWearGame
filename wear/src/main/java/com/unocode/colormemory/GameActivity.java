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
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GameActivity extends Activity {

    public TextView mTextView, tvWhoseTurn, tvScore, tvLives, tvTimeLimit, tvReverse, tvAnyOrder, tvInverse;
    private Integer currentCount;
    private Integer currentScore;
    private Integer playerIndex;
    public Button btnRed, btnYellow, btnBlue, btnGreen;
    public Activity context;
    public ArrayList<Integer> sequence;
    public List subsequence;
    public final int START_GAME_COUNT = 1;
    SharedPreferences sharedpreferences;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String highscore = "highscore";
    public static final String highcount = "highcount";
    public static final String DifficultySetting = "difficulty";
    public static final String TimeLimit = "time_limit";
    public static final String Lives = "lives";
    public static final String RandomColors = "random_colors";
    public static final String Randomize = "randomize_list";
    public static final String Reverse = "reverse";
    public static final String AnyOrder = "any_order";
    public static final String DoubleSpeed = "double_speed";
    public static final String Inverse = "inverse";
    public static final String TotalGamesPlayed = "total_games_played";


    public int difficultySetAt;
    public boolean timeLimitSet;
    public boolean livesSet;
    public boolean failed;
    public boolean randomColors;
    public boolean reverseSet;
    public boolean randomizeSet;
    public boolean doubleSpeedSet;
    public boolean inverseSet;
    public boolean anyOrderSet;

    public int timeLimit;
    public int livesCount, currentLives;
    public CountDownTimer cdt;
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

        scoreMultipler = 20;

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        difficultySetAt = sharedpreferences.getInt(DifficultySetting, 0);
        timeLimitSet = sharedpreferences.getBoolean(TimeLimit, false);
        livesSet = sharedpreferences.getBoolean(Lives, false);
        failed = false;
        randomizeSet = sharedpreferences.getBoolean(Randomize, false);
        randomColors = sharedpreferences.getBoolean(RandomColors, false);
        reverseSet = sharedpreferences.getBoolean(Reverse, false);
        doubleSpeedSet = sharedpreferences.getBoolean(DoubleSpeed, false);
        inverseSet = sharedpreferences.getBoolean(Inverse, false);
        //hard code to test
        anyOrderSet = sharedpreferences.getBoolean(AnyOrder, false);

            switch (difficultySetAt) {
                case 0:
                    gameSpeed = 1000;
                    timeLimit = 750;
                    livesCount = 5;
                    break;
                case 1:
                    gameSpeed = 750;
                    timeLimit = 500;
                    scoreMultipler += 2;
                    livesCount = 3;
                    break;
                case 2:
                    gameSpeed = 350;
                    timeLimit = 350;
                    scoreMultipler += 5;
                    livesCount = 1;
                    break;
                default:
                    gameSpeed = 1000;
                    timeLimit = 4000;
                    livesCount = 5;
                    break;
            }

        currentLives = livesCount;
        if(randomizeSet) scoreMultipler += 3;
        if(randomColors) scoreMultipler += 5;
        if(doubleSpeedSet) scoreMultipler += 5;
        if(inverseSet) scoreMultipler += 5;
        if(reverseSet) scoreMultipler += 8;
        if(anyOrderSet) scoreMultipler -= 8;
        if(livesSet) scoreMultipler -= 5;
        if(timeLimitSet) scoreMultipler += 3;

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

        tvLives = (TextView) findViewById(R.id.tvLives);
        if(livesSet){
            String livesText = getResources().getString(R.string.lives) + ": " +  currentLives;
            tvLives.setText(livesText);
        }

        tvReverse = (TextView) findViewById(R.id.tvReverse);
        if(reverseSet){
            tvReverse.setText(R.string.reverse);
        }else if(anyOrderSet){
            tvReverse.setText(R.string.any_order);
        }

        tvInverse = (TextView) findViewById(R.id.tvInverse);
        if(inverseSet) tvInverse.setText(R.string.inverse);


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
        Log.v("Game Start", "Randomize Lists Set: " + randomizeSet);
        Log.v("Game Start", "Randomize Colors Set: " + randomColors);
        Log.v("Game Start", "Randomize Lives Set: " + livesSet);

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
        //log games played
        if(gameCount == 1){
            int totalGamesPlayed = sharedpreferences.getInt(TotalGamesPlayed, 0);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            Log.d("gameloop", "prev total games played:" + totalGamesPlayed);
            editor.putInt(TotalGamesPlayed, totalGamesPlayed + 1);
            editor.apply();
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
            if(doubleSpeedSet && gameCount > 1){
                sequence.add(value);
            }
        } else {
            Log.d("gameloop", "else randomize is true and liveset is false");
            sequence.clear();//clear list
            for (int i = 0; i < gameCount; ++i) {
                sequence.add(value);
                value = rand.nextInt(4) + 1;//rand between 1-4
            }
        }
        subsequence = (List)sequence.clone();
        String sequenceString = "Sequence: ";
        for (int i = 0; i < sequence.size(); ++i) {//print the whole sequence for this round
            sequenceString = sequenceString + sequence.get(i) + ",";
        }

        Log.d("gameloop", sequenceString);
        //chance try 4-4-17 to run on ui thread
        if (randomColors) {
            randomizeButtonColors();
            callSequence(context, sequence);
        } else {
            callSequence(context, sequence);
        }
    }

    public void randomizeButtonColors() {
        //1-red,2-green,3-blue,4-yellow
        Log.d("randomizeButtonColors", "start");
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
        Log.d("callSequence", "start");

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

        if (sequence.size() == 1){//to fix issue with timer starting too soon on hard
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toggleAllButtons(true);//enable buttons
                    if (timeLimitSet) {
                        cdt = new CountDownTimer(1000, 1) {
                            public void onTick(long millisUntilFinished) {
                                String timeLimitString = "Time: " + String.format(Locale.US,"%d.%d", TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished),millisUntilFinished);
                                tvTimeLimit.setText(timeLimitString);
                            }

                            public void onFinish() {
                                if(livesSet && currentLives > 0){
                                    Log.d("increaseScore", "Failed but with lives");
                                    failed = true;
                                    currentLives--;
                                    String livesText = getResources().getString(R.string.lives) + ": " +  currentLives;
                                    tvLives.setText(livesText);
                                    playerIndex = 0;//reset player index
                                    showContinueMessage();
                                }
                                else {
                                    Log.d("onFinish", "failed");
                                    showFailedMessage();
                                }
                            }
                        }.start();
                    }
                }
            }, 1000 * sequence.size());
        }
        else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toggleAllButtons(true);//enable buttons
                    if (timeLimitSet) {
                        cdt = new CountDownTimer(1000 + (timeLimit * sequence.size()), 1) {
                            public void onTick(long millisUntilFinished) {
                                String timeLimitString = "Time: " + String.format(Locale.US,"%d.%d", TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished),millisUntilFinished);
                                tvTimeLimit.setText(timeLimitString);
                            }

                            public void onFinish() {
                                if(livesSet && currentLives > 0){
                                    Log.d("increaseScore", "Failed but with lives");
                                    failed = true;
                                    currentLives--;
                                    String livesText = getResources().getString(R.string.lives) + ": " +  currentLives;
                                    tvLives.setText(livesText);
                                    playerIndex = 0;//reset player index
                                    showContinueMessage();
                                }
                                else {
                                    Log.d("onFinish", "failed");
                                    showFailedMessage();
                                }
                            }
                        }.start();
                    }
                }
            }, gameSpeed * sequence.size());
        }


    }

    class ClickThread extends Thread {
        private WeakReference<Activity> mActivity;
        private Button btn;
        private Button btn1, btn2, btn3;
        private int[] otherBtnIds;
        private int[] btnIds;

        ClickThread(Activity activity, int btnId) {
            Log.d("ClickThread()", "start");
            mActivity = new WeakReference<>(activity);
            btnIds = new int[] {R.id.btnRed, R.id.btnGreen, R.id.btnBlue, R.id.btnYellow};
            otherBtnIds = new int[4];
            if(!inverseSet){
                btn = (Button) activity.findViewById(btnId);
            } else {
                int otherBtnIdCount = 0;
                for(int i = 0; i < btnIds.length; ++i){

                    if(btnId != btnIds[i]){
                        otherBtnIds[otherBtnIdCount] = btnIds[i];
                        otherBtnIdCount++;
                    }
                }
                btn1 = (Button) activity.findViewById(otherBtnIds[0]);
                btn2 = (Button) activity.findViewById(otherBtnIds[1]);
                btn3 = (Button) activity.findViewById(otherBtnIds[2]);
            }
        }

        @Override
        public void run() {
            Log.d("ClickThread run()", "start");
            Activity activity = mActivity.get();
            if (activity != null) {
                if(!inverseSet){
                    changeBackgroundColor(btn, false);
                } else {
                    changeBackgroundColor(btn1, false);
                    changeBackgroundColor(btn2, false);
                    changeBackgroundColor(btn3, false);
                }
            }
        }
    }

    public int calculateScore() {
        Log.d("calculateScore()", "currentScore " + currentCount);
        Log.d("calculateScore()", "scoreMultipler " + scoreMultipler);

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
        int expected;

        if(reverseSet){
            expected =  sequence.get(sequence.size() - (playerIndex+1));
        }else{
            expected = sequence.get(playerIndex);
        }
        Log.d("increaseScore", String.format(Locale.US, "Entered: %d. Expected: %d", selectedColor, expected));

        //testing any order
        //List subsequence = sequence.subList(playerIndex, sequence.size());
        if(anyOrderSet){
            if(subsequence.contains(selectedColor)){
                //remove the
                subsequence.remove(subsequence.indexOf(selectedColor));
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

                    if(doubleSpeedSet){
                        currentCount += 2;//
                    } else{
                        currentCount++;
                    }

                    //save highcount
                    if (sharedpreferences.getInt("highcount", 0) < currentCount) {
                        Log.d("increaseCount", "new highcount is " + currentCount);

                        editor.putInt(highcount, currentCount);
                        editor.apply();
                    }

                    playerIndex = 0;//reset player index

                    GameLoop(currentCount);
                } else {
                    playerIndex++;
                }
            } else if (livesSet && currentLives > 0) {//fail but with lives
                Log.d("increaseScore", "Failed but with lives");
                failed = true;
                currentLives--;
                String livesText = getResources().getString(R.string.lives) + ": " +  currentLives;
                tvLives.setText(livesText);
                if (cdt != null) cdt.cancel();
                playerIndex = 0;//reset player index
                showContinueMessage();
            } else {
                Log.d("increaseScore", "Failed");
                if (cdt != null) cdt.cancel();
                showFailedMessage();
            }
        } else if (expected == selectedColor) {
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

                if(doubleSpeedSet){
                    currentCount += 2;//
                } else{
                    currentCount++;
                }

                //save highcount
                if (sharedpreferences.getInt("highcount", 0) < currentCount) {
                    Log.d("increaseCount", "new highcount is " + currentCount);

                    editor.putInt(highcount, currentCount);
                    editor.apply();
                }

                playerIndex = 0;//reset player index

                GameLoop(currentCount);
            } else {
                playerIndex++;
            }

        } else if (livesSet && currentLives > 0) {//fail but with lives
            Log.d("increaseScore", "Failed but with lives");
            failed = true;
            currentLives--;
            String livesText = getResources().getString(R.string.lives) + ": " +  currentLives;
            tvLives.setText(livesText);
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
        String livesText = getResources().getString(R.string.lives) + ": " +  currentLives;
        if(livesSet) tvLives.setText(livesText);

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
        Log.d("changeBackgroundColor", "start");
        //get the button
        final Button localButton = ((Button) v);

        //get current color of button (for button color change setting)
        final Drawable background = localButton.getBackground();

        localButton.setPressed(true);

        int countdownTimerNum;
        if (isUserClick) {
            //Log.d("changeBackgroundColor", "isUserClick true");
            countdownTimerNum = 50;
        } else {
            //Log.d("changeBackgroundColor", "isUserClick false");
            countdownTimerNum = 200;
        }

        new CountDownTimer(countdownTimerNum, countdownTimerNum) {//200ms
            @Override
            public void onTick(long arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onFinish() {
                //change color back to original color
                localButton.setPressed(false);
                if(randomColors) {
                    Log.d("if(randomColors)", "setting background color to previous");
                    localButton.setBackground(background);
                }
            }
        }.start();
    }
}
