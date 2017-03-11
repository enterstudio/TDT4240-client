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


public class ExitFragment extends Fragment {

    private TextView playerWonTextView;
    private int playerWon;


    public ExitFragment() {
        // Required empty public constructor
    }

    public int getPlayerWon(){
        return playerWon;
    }

    public void setPlayerWon(){
        playerWon=3;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exit, container, false);
        playerWonTextView = (TextView) rootView.findViewById(R.id.gamePinTextView);


        playerWonTextView.setText("Player " + getPlayerWon() + " has won!");

        return rootView;

    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
