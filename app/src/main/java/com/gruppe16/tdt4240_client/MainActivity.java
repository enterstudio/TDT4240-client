package com.gruppe16.tdt4240_client;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gruppe16.tdt4240_client.fragments.MenuFragment;
import com.gruppe16.tdt4240_client.interfaces.OnGoToView;

public class MainActivity extends AppCompatActivity implements OnGoToView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeStartFragment();
    }

    public void initializeStartFragment(){
        MenuFragment fragment = MenuFragment.newInstance();
        //DrawFragment fragment = DrawFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        fm
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }

    @Override
    public void goToJoinGameView() {
        FragmentChanger.goToJoinGameView(this);
    }

    @Override
    public void goToDrawView() {
        FragmentChanger.goToDrawView(this);
    }

    @Override
    public void goToWaitingView() {
        FragmentChanger.goToWaitingView(this);
    }

    @Override
    public void goToGuessView() {
        FragmentChanger.goToGuessView(this);
    }

    @Override
    public void goToFinishedView() {
        FragmentChanger.goToFinishedView(this);
    }

    @Override
    public void goToSlideShowView() {
        FragmentChanger.goToSlideshowView(this);
    }

    @Override
    public void goToScoreboardView() {
        FragmentChanger.goToScoreboardView(this);
    }

    @Override
    public void goToCreateGameView() {
        FragmentChanger.goToCreateGameView(this);
    }



}
