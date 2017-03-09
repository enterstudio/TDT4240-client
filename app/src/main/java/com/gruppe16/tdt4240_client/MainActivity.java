package com.gruppe16.tdt4240_client;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gruppe16.tdt4240_client.fragments.MenuFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void initializeStartFragment(){
        MenuFragment fragment = MenuFragment.newInstance("hey", "yo");
        FragmentManager fm = getSupportFragmentManager();
        fm
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }
}
