package com.gruppe16.tdt4240_client;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import com.gruppe16.tdt4240_client.fragments.CreateGameFragment;
import com.gruppe16.tdt4240_client.fragments.DrawFragment;
import com.gruppe16.tdt4240_client.fragments.ExitFragment;
import com.gruppe16.tdt4240_client.fragments.FinishedFragment;
import com.gruppe16.tdt4240_client.fragments.GuessFragment;
import com.gruppe16.tdt4240_client.fragments.JoinGameFragment;
import com.gruppe16.tdt4240_client.fragments.ScoreboardFragment;
import com.gruppe16.tdt4240_client.fragments.SlideShowFragment;
import com.gruppe16.tdt4240_client.fragments.WaitingFragment;

import java.util.ArrayList;

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

    public static void goToGuessView(FragmentActivity activity){
        GuessFragment fragment = GuessFragment.newInstance();
        FragmentManager fm = activity.getSupportFragmentManager();
        fm
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public static void goToScoreboardView(FragmentActivity activity){
        ScoreboardFragment fragment = ScoreboardFragment.newInstance();
        FragmentManager fm = activity.getSupportFragmentManager();
        fm
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public static void goToExitView(String myPlayerId, String gamePin, ArrayList<Integer> indexOfWinners, FragmentActivity activity){
        ExitFragment fragment = ExitFragment.newInstance();
        FragmentManager fm = activity.getSupportFragmentManager();
        fm
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public static void goToFinishedView(FragmentActivity activity){
        FinishedFragment fragment = FinishedFragment.newInstance();
        FragmentManager fm = activity.getSupportFragmentManager();
        fm
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

}

