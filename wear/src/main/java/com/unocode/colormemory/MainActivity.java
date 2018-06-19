package com.unocode.colormemory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.wear.widget.drawer.WearableNavigationDrawerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity implements
        WearableNavigationDrawerView.OnItemSelectedListener{

    private TextView tvHighScore;
    private TextView tvMultiplier;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String highscoreSetting = "highscore";
    public static final String DifficultySetting = "difficulty";
    public static final String TimeLimit = "time_limit";
    public static final String Lives = "lives";
    public static final String RandomColors = "random_colors";
    public static final String Randomize = "randomize_list";
    public static final String Reverse = "reverse";
    public static final String AnyOrder = "any_order";
    public static final String DoubleSpeed = "double_speed";
    public static final String Inverse = "inverse";

    public int difficultySetAt;
    public boolean timeLimitSet;
    public boolean livesSet;
    public boolean randomColors;
    public boolean reverseSet;
    public boolean anyOrderSet;
    public boolean randomizeSet;
    public boolean doubleSpeedSet;
    public boolean inverseSet;

    private WearableNavigationDrawerView mWearableNavigationDrawer;
    private ArrayList<NavMenu> mNavMenu;
    private int navMenuItemSelected;

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate", "Main activity creating");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavMenu = initializeNavMenu();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        tvHighScore = findViewById(R.id.highscore);
        tvHighScore.setText(String.format(Locale.US, "Highscore : %d", sharedpreferences.getInt(highscoreSetting, 1)));

        tvMultiplier = findViewById(R.id.multiplier);
        tvMultiplier.setText(String.format(Locale.US, "Multiplier : %d", GetMultiplier()));

        // Top Navigation Drawer
        mWearableNavigationDrawer = findViewById(R.id.top_navigation_drawer);
        mWearableNavigationDrawer.setAdapter(new NavigationAdapter(this, mNavMenu));
        // Peeks navigation drawer on the top.
        mWearableNavigationDrawer.getController().peekDrawer();
        mWearableNavigationDrawer.addOnItemSelectedListener(this);

        navMenuItemSelected = -1;
    }

    private ArrayList<NavMenu> initializeNavMenu() {
        ArrayList<NavMenu> navMenu = new ArrayList<>();
        String[] navMenuNames = getResources().getStringArray(R.array.navmenu_array_names);

        for (int i = 0; i < navMenuNames.length; i++) {
            String menuItem = navMenuNames[i];
            int planetResourceId =
                    getResources().getIdentifier(menuItem, "array", getPackageName());
            String[] planetInformation = getResources().getStringArray(planetResourceId);

            navMenu.add(new NavMenu(
                    planetInformation[0],   // Name
                    planetInformation[1])); // NavIcon
        }

        return navMenu;
    }

    /*
    private final class NavigationAdapter
            extends WearableNavigationDrawerView.WearableNavigationDrawerAdapter {

        private final Context mContext;

        public NavigationAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return mNavMenu.size();
        }

        @Override
        public String getItemText(int pos) {
            return mNavMenu.get(pos).getName();
        }

        @Override
        public Drawable getItemDrawable(int pos) {
            String navigationIcon = mNavMenu.get(pos).getNavigationIcon();

            int drawableNavigationIconId =
                    getResources().getIdentifier(navigationIcon, "mipmap", getPackageName());

            //return mContext.getDrawable(drawableNavigationIconId);
            return ResourcesCompat.getDrawable(getResources(), drawableNavigationIconId, null);
        }
    }*/

    // Updates content when user changes between items in the navigation drawer.
    @Override
    public void onItemSelected(int position) {
        Log.d(TAG, "onItemSelected(): " + position);
        switch(position){
            case 0:
                //if(navMenuItemSelected == position)
                    showSettings();
                //else {
                //    navMenuItemSelected = position;
                //}
                break;
            case 1:
                //if(navMenuItemSelected == position)
                    showAbout();
                //else {
                    navMenuItemSelected = position;
                //}
                break;
        }
    }


    @Override
    public void onResume(){
        Log.d("onResume", "Main activity resuming");
        Log.d("Saved Highscore", "" + sharedpreferences.getInt(highscoreSetting, 1));

        tvHighScore.setText(String.format(Locale.US, "Highscore : %d", sharedpreferences.getInt(highscoreSetting, 1)));
        tvMultiplier.setText(String.format(Locale.US, "Multiplier : %d", GetMultiplier()));

        super.onResume();
    }

    public void startGame(View v)
    {
        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
        startActivity(intent);
    }

    public int GetMultiplier(){
        int scoreMultiplier = 20;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        difficultySetAt = sharedpreferences.getInt(DifficultySetting, 0);
        timeLimitSet = sharedpreferences.getBoolean(TimeLimit, false);
        livesSet = sharedpreferences.getBoolean(Lives, false);
        randomizeSet = sharedpreferences.getBoolean(Randomize, false);
        randomColors = sharedpreferences.getBoolean(RandomColors, false);
        reverseSet = sharedpreferences.getBoolean(Reverse, false);
        anyOrderSet = sharedpreferences.getBoolean(AnyOrder, false);
        doubleSpeedSet = sharedpreferences.getBoolean(DoubleSpeed, false);
        inverseSet = sharedpreferences.getBoolean(Inverse, false);

        switch (difficultySetAt) {
            case 1:
                scoreMultiplier += 2;
                break;
            case 2:
                scoreMultiplier += 5;
                break;
            default:
                break;
        }

        if(randomizeSet) scoreMultiplier += 3;
        if(randomColors) scoreMultiplier += 5;
        if(doubleSpeedSet) scoreMultiplier += 5;
        if(timeLimitSet) scoreMultiplier += 3;
        if(livesSet) scoreMultiplier -= 5;
        if(reverseSet) scoreMultiplier += 8;
        if(anyOrderSet) scoreMultiplier -= 8;
        if(inverseSet) scoreMultiplier += 5;

        return scoreMultiplier;
    }

    public void showSettings()
    {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    public void showAbout()
    {
        Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
        startActivity(intent);
    }
}
