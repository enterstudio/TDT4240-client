package com.gruppe16.tdt4240_client.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.Response;
import com.gruppe16.tdt4240_client.FragmentChanger;
import com.gruppe16.tdt4240_client.GameState;
import com.gruppe16.tdt4240_client.NetworkAbstraction;
import com.gruppe16.tdt4240_client.R;

import org.json.JSONException;
import org.json.JSONObject;

public class JoinGameFragment extends Fragment implements Response.Listener<JSONObject> {

    private Button joinGameButton;
    private EditText gamePinInput;
    private GameState gameState;

    // Required empty public constructor
    public JoinGameFragment() {}

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_join_game, container, false);
        gamePinInput = (EditText) rootView.findViewById(R.id.gamePinInput);
        joinGameButton = (Button) rootView.findViewById(R.id.joinGameBtn);
        joinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed();
            }
        });
        gameState = GameState.getInstance();
        return rootView;
    }

    public void onButtonPressed() {
        String gamePin = gamePinInput.getText().toString();
        gameState.setGamePin(gamePin);
        Response.Listener<JSONObject> listener = this;
        NetworkAbstraction.getInstance(getContext()).joinGame(listener);
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            String myPlayerId = response.getString("myPlayerId");
            gameState.setMyPlayerId(myPlayerId);
            FragmentChanger.goToWaitingView(getActivity());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
