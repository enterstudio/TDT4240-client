package com.gruppe16.tdt4240_client.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gruppe16.tdt4240_client.FragmentChanger;
import com.gruppe16.tdt4240_client.R;

public class CreateGameFragment extends Fragment {

    private Button startGameButton;
    private TextView gamePinTextView;
    private TextView playersCountTextView;
    private int gamePin;
    private int playersCount;


    public CreateGameFragment() {
        // Required empty public constructor
        setPin();
        setPlayers();
    }

    public static CreateGameFragment newInstance() {
        CreateGameFragment fragment = new CreateGameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public int getPin(){ return gamePin; }

    public void setPin(){ gamePin = 12345; }

    public int getPlayers(){ return playersCount; }

    public void setPlayers(){ playersCount = 3; }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_game, container, false);
        gamePinTextView = (TextView) rootView.findViewById(R.id.gamePinTextView);
        playersCountTextView = (TextView) rootView.findViewById(R.id.playersCountTextView);
        startGameButton = (Button) rootView.findViewById(R.id.startGameButton);

        gamePinTextView.setText("PIN: " + getPin());
        playersCountTextView.setText("Player: " + getPlayers());

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {FragmentChanger.goToDrawView(getActivity());
            }
        });

        return rootView;
    }
}
