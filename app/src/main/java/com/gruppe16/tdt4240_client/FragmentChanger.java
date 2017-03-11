package com.gruppe16.tdt4240_client;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.gruppe16.tdt4240_client.fragments.CreateGameFragment;
import com.gruppe16.tdt4240_client.fragments.DrawFragment;
import com.gruppe16.tdt4240_client.fragments.JoinGameFragment;
import com.gruppe16.tdt4240_client.fragments.MenuFragment;
import com.gruppe16.tdt4240_client.fragments.SlideShowFragment;
import com.gruppe16.tdt4240_client.fragments.WaitingFragment;

/**
 * Created by Camilla on 09.03.2017.
 */

public class FragmentChanger {

    public static void goToCreateGameView(FragmentActivity activity) {
        CreateGameFragment fragment = CreateGameFragment.newInstance();
        FragmentManager fm = activity.getSupportFragmentManager();
        fm
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }


    public static void goToSlideshowView(FragmentActivity activity) {
        SlideShowFragment fragment = SlideShowFragment.newInstance();
        FragmentManager fm = activity.getSupportFragmentManager();
        fm
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public static void goToJoinGameView (FragmentActivity activity){
        JoinGameFragment fragment = JoinGameFragment.newInstance();
        FragmentManager fm = activity.getSupportFragmentManager();
        fm
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public static void goToWaitingView(FragmentActivity activity) {
        WaitingFragment fragment = WaitingFragment.newInstance();
        FragmentManager fm = activity.getSupportFragmentManager();
        fm
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public static void goToDrawView(FragmentActivity activity){
        DrawFragment fragment = DrawFragment.newInstance();
        FragmentManager fm = activity.getSupportFragmentManager();
        fm
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}

