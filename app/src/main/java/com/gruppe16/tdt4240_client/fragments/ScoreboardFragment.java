package com.gruppe16.tdt4240_client.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gruppe16.tdt4240_client.R;




public class ScoreboardFragment extends Fragment {

    private TextView roundNumberTextView;
    private int roundNumber;
    private TextView playerNumberTextView;
    private int playerNumber;


    public ScoreboardFragment() {
        // Required empty public constructor
        setRoundNumber();
        setPlayerNumber();
    }

    public static ScoreboardFragment newInstance() {
        ScoreboardFragment fragment = new ScoreboardFragment();
        return fragment;
    }


    public int getRoundNumber(){
        return roundNumber;
    }
    public int getPlayerNumber(){
        return playerNumber;
    }
    public void setRoundNumber(){
        roundNumber=5;
    }
    public void setPlayerNumber(){
        playerNumber=3;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_scoreboard, container, false);

        roundNumberTextView = (TextView) rootView.findViewById(R.id.roundNumberTextView);
        playerNumberTextView = (TextView) rootView.findViewById(R.id.playerNumberTextView);

        roundNumberTextView.setText("Round " + getRoundNumber());
        playerNumberTextView.setText("Player " + getPlayerNumber());




        return rootView;
    }


}
