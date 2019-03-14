package com.example.callingandmessaging;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
//        position = position + 1;
//        Log.d("get Item");
        switch (position){
            case 0:
                SeeCallTimerFragment seeCallTimerFragment = new SeeCallTimerFragment();
                return seeCallTimerFragment;

            case 1:
                SeeMessageTimerFragment seeMessageTimerFragment = new SeeMessageTimerFragment();
                return seeMessageTimerFragment;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Call Timers";

            case 1:
                return "Message Timers";
        }
        return "";
    }
}
