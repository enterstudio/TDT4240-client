package com.gruppe16.tdt4240_client.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gruppe16.tdt4240_client.R;
import com.gruppe16.tdt4240_client.interfaces.OnGoToView;

public class MenuFragment extends Fragment {

    private Button createGameButton;
    private Button joinGameButton;
    private OnGoToView onGoToView;

    // Required empty public constructor
    public MenuFragment() {}

    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        createGameButton = (Button) rootView.findViewById(R.id.createGameButton);
        joinGameButton = (Button) rootView.findViewById(R.id.joinGameButton);

        createGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoToView.goToCreateGameView();
            }
        });

        joinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoToView.goToJoinGameView();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            onGoToView = (OnGoToView) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnGoToView");
        }
    }



}
