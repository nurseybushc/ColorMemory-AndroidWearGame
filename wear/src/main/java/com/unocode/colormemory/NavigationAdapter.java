package com.unocode.colormemory;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.wear.widget.drawer.WearableNavigationDrawerView;

import java.util.ArrayList;

public final class NavigationAdapter extends WearableNavigationDrawerView.WearableNavigationDrawerAdapter {

    private Context mContext;
    private ArrayList<NavMenu> mNavMenu;

    public NavigationAdapter(Context context, ArrayList<NavMenu> navMenu) {
        mContext = context;
        mNavMenu = navMenu;
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
                mContext.getResources().getIdentifier(navigationIcon, "mipmap", mContext.getPackageName());

        return ResourcesCompat.getDrawable(mContext.getResources(), drawableNavigationIconId, null);
    }
}
