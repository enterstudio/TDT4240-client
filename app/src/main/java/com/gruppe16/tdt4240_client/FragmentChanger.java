package com.gruppe16.tdt4240_client;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.gruppe16.tdt4240_client.fragments.MenuFragment;

/**
 * Created by Camilla on 09.03.2017.
 */

public class FragmentChanger {

    public static void goToCreateGameView(MainActivity activity){
        MenuFragment fragment = MenuFragment.newInstance();
        FragmentManager fm = activity.getSupportFragmentManager();
        fm
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public static void goToJoinGameView(){

    }
}
