package com.cqut.faymong.betsoftware.entity;

import com.cqut.faymong.betsoftware.R;

/**
 * Created by iiro on 7.6.2016.
 */
public class TabMessage {
    public static int get(int menuItemId, boolean isReselection) {
        String message = "Content for ";
        int position =0;

        switch (menuItemId) {
            case R.id.tab_livebroadcast_demo:
                message += "favorites";
                position =0;
                break;
            case R.id.tab_betrecord:
                message += "nearby";
                position =1;
                break;
            case R.id.tab_mine:
                message += "friends";
                position =3;
                break;
            case R.id.tab_schedule:
                message += "restaurants";
                position =2;
                break;
        }

        if (isReselection) {
            message += " WAS RESELECTED! YAY!";
        }

        return position;
    }
}
