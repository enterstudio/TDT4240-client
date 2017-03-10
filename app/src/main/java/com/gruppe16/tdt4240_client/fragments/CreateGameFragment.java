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

import com.gruppe16.tdt4240_client.R;

public class CreateGameFragment extends Fragment {

    private Button startGameButton;
    private TextView gamePin;
    private TextView playersCount;

    public CreateGameFragment() {
        // Required empty public constructor
    }

    public static CreateGameFragment newInstance() {
        CreateGameFragment fragment = new CreateGameFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_game, container, false);
        startGameButton = (Button) rootView.findViewById(R.id.startGameButton);
        gamePin = (TextView) rootView.findViewById(R.id.gamePin);
        playersCount = (TextView) rootView.findViewById(R.id.playersCount);


        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FragmentChanger.goToDrawView(getActivity());
            }
        });

        return rootView;

    }
}
