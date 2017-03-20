package com.gruppe16.tdt4240_client.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gruppe16.tdt4240_client.FragmentChanger;
import com.gruppe16.tdt4240_client.R;

public class JoinGameFragment extends Fragment {

    private Button joinGameButton;
    private EditText gamePinInput;
    private JoinGameByPinListener mListener;

    public JoinGameFragment() {
        // Required empty public constructor
    }

    public static JoinGameFragment newInstance() {
        JoinGameFragment fragment = new JoinGameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_join_game, container, false);
        gamePinInput = (EditText) rootView.findViewById(R.id.gamePinInput);
        joinGameButton = (Button) rootView.findViewById(R.id.joinGameBtn);
        joinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed();
            }
        });

        return rootView;
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onJoinGame(gamePinInput.getText().toString());
        }
        FragmentChanger.goToWaitingView(getActivity(), gamePinInput.getText().toString());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof JoinGameByPinListener) {
            mListener = (JoinGameByPinListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement JoinGameByPinListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface JoinGameByPinListener {
        void onJoinGame(String gamePin);
    }
}
