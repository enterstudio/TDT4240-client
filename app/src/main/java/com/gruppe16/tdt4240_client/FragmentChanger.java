package com.gruppe16.tdt4240_client;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.gruppe16.tdt4240_client.fragments.CreateGameFragment;
import com.gruppe16.tdt4240_client.fragments.DrawFragment;
import com.gruppe16.tdt4240_client.fragments.GuessFragment;
import com.gruppe16.tdt4240_client.fragments.JoinGameFragment;
import com.gruppe16.tdt4240_client.fragments.MenuFragment;
import com.gruppe16.tdt4240_client.fragments.SlideShowFragment;
import com.gruppe16.tdt4240_client.fragments.WaitingFragment;

import org.json.JSONObject;

import static com.gruppe16.tdt4240_client.MainActivity.round;

/**
 * Created by Camilla on 09.03.2017.
 */

public class FragmentChanger {

    public static void goToCreateGameView(FragmentActivity activity) {
        CreateGameFragment fragment = CreateGameFragment.newInstance();
        FragmentManager fm = activity.getSupportFragmentManager();
        fm
                .beginTransaction()
                .addToBackStack(null)
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
                .addToBackStack(null)
                .replace(R.id.container, fragment)
                .commit();
    }

    public static void goToWaitingView(FragmentActivity activity, String gamePin) {
        WaitingFragment fragment = WaitingFragment.newInstance();
        Bundle args = new Bundle();
        args.putString("gamePin", gamePin);
        fragment.setArguments(args);
        FragmentManager fm = activity.getSupportFragmentManager();
        fm
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public static void goToDrawView(FragmentActivity activity, String gamePin, String playerID){
        round++;
        System.out.println("Current round: " + round);
        DrawFragment fragment = DrawFragment.newInstance();
        Bundle args = new Bundle();
        args.putString("gamePin", gamePin);
        args.putString("playerID", playerID);
        fragment.setArguments(args);
        FragmentManager fm = activity.getSupportFragmentManager();
        fm
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public static void goToGuessView(FragmentActivity activity){
        round++;
        System.out.println("Current round: " + round);
        GuessFragment fragment = GuessFragment.newInstance();
        FragmentManager fm = activity.getSupportFragmentManager();
        fm
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}

