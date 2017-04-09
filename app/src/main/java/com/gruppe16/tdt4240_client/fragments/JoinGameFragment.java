package com.gruppe16.tdt4240_client.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.Response;
import com.gruppe16.tdt4240_client.GameState;
import com.gruppe16.tdt4240_client.NetworkAbstraction;
import com.gruppe16.tdt4240_client.R;
import com.gruppe16.tdt4240_client.interfaces.OnGoToView;

import org.json.JSONException;
import org.json.JSONObject;

public class JoinGameFragment extends Fragment {

    private Button joinGameButton;
    private EditText gamePinInput;
    private GameState gameState;
    private OnGoToView onGoToView;

    private Response.Listener<JSONObject> joinGameListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            if(isAdded()){
                if(isAdded()) {
                    try {
                        String myPlayerId = response.getString("myPlayerId");
                        gameState.setMyPlayerId(myPlayerId);
                        onGoToView.goToWaitingView();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

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
        NetworkAbstraction.getInstance(getContext()).joinGame(joinGameListener);
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
