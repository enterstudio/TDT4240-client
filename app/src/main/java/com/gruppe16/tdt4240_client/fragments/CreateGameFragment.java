package com.gruppe16.tdt4240_client.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.Response;
import com.gruppe16.tdt4240_client.GameState;
import com.gruppe16.tdt4240_client.NetworkAbstraction;
import com.gruppe16.tdt4240_client.R;
import com.gruppe16.tdt4240_client.interfaces.OnGoToView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Timer;
import java.util.TimerTask;


public class CreateGameFragment extends Fragment {

    private TextView playersCountTextView;
    private TextView gamePinTextView;
    private Button startGameButton;
    private Timer playerPollTimer;
    private GameState gameState;
    private OnGoToView onGoToView;
    private JSONArray players;

    private Response.Listener<JSONObject> isGameStartedListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            if(isAdded()) {
                GameState.getInstance().setNumberOfPlayers(players.length());
                onGoToView.goToDrawView();
            }
        }
    };

    private Response.Listener<JSONObject> pollForGameListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            if(isAdded()) {
                try {
                    // Process and display number of players
                    players = response.getJSONArray("players");
                    processNumberOfPlayers(players.length());
                    playersCountTextView.setText(getString(R.string.players_connected) + " " + players.length());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private Response.Listener<JSONObject> gameCreatedListener = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            if (isAdded()) {
                try {
                    // Set the myPlayerId in shared preferences
                    String myPlayerId = "0";
                    gameState.setMyPlayerId(myPlayerId);
                    // Set the gamePin
                    String gamePin = response.getString("gamePin");
                    gameState.setGamePin(gamePin);
                    gamePinTextView.setText(getString(R.string.pin) + " " + gamePin);
                    pollForGame();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    // Required empty public constructor
    public CreateGameFragment() {}

    public static CreateGameFragment newInstance() {
        CreateGameFragment fragment = new CreateGameFragment();
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_game, container, false);
        gamePinTextView = (TextView) rootView.findViewById(R.id.gamePinTextView);
        playersCountTextView = (TextView) rootView.findViewById(R.id.playersCountTextView);
        startGameButton = (Button) rootView.findViewById(R.id.startGameButton);

        gameState = GameState.getInstance();

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            playerPollTimer.cancel();
            NetworkAbstraction.getInstance(getContext()).startGame(isGameStartedListener);
            }
        });

        startGameButton.setEnabled(false);

        // Create the game
        createGame();

        return rootView;
    }

    private void createGame(){
        /* Request new gamePin from server */
        NetworkAbstraction.getInstance(getContext()).createGame(gameCreatedListener);
    }

    private void pollForGame(){
        System.out.println("Polling for game...");
        playerPollTimer = new Timer();
        playerPollTimer.scheduleAtFixedRate( new TimerTask() {
            @Override
            public void run() {
                NetworkAbstraction.getInstance(getContext()).getGame(pollForGameListener);
            }
        }, 0, 1000);
    }

    private void processNumberOfPlayers(int players){
        int minPlayers = 2; // TODO: Set this to correct number
        if (players % 2 == 0 && players >= minPlayers ){
            startGameButton.setEnabled(true);
            startGameButton.setAlpha(1f);
        }
        else {
            startGameButton.setAlpha(0.5f);
            startGameButton.setEnabled(false);
        }

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
