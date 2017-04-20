package com.gruppe16.tdt4240_client.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.android.volley.Response;
import com.gruppe16.tdt4240_client.GameState;
import com.gruppe16.tdt4240_client.NetworkAbstraction;
import com.gruppe16.tdt4240_client.R;
import com.gruppe16.tdt4240_client.interfaces.OnGoToView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class ScoreboardFragment extends Fragment implements Response.Listener<JSONObject>, View.OnClickListener {

    private Button exitButton;
    private TableLayout tableLayout;
    private OnGoToView onGoToView;
    private Timer gameScoreReceivedPollTimer;
    private JSONArray scores;

    // Required empty public constructor
    public ScoreboardFragment() {}

    public static ScoreboardFragment newInstance() {
        return new ScoreboardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_scoreboard, container, false);

        TextView playerNumberTextView = (TextView) rootView.findViewById(R.id.playerNumberTextView);
        tableLayout = (TableLayout) rootView.findViewById(R.id.scoreboard);

        String myPlayerId = GameState.getInstance().getMyPlayerId();
        playerNumberTextView.setText(getString(R.string.player) + " " + myPlayerId);

        exitButton = (Button) rootView.findViewById(R.id.exitButton);
        exitButton.setEnabled(false);
        exitButton.setClickable(false);
        exitButton.setAlpha(0.3f);
        exitButton.setOnClickListener(this);
        exitButton.setVisibility(View.VISIBLE);

        setPollingForGameFinished();

        return rootView;
    }


    private void setPollingForGameFinished(){
        gameScoreReceivedPollTimer = new Timer();
        final Response.Listener<JSONObject> listener = this;
        gameScoreReceivedPollTimer.scheduleAtFixedRate( new TimerTask() {
            @Override
            public void run() {
                NetworkAbstraction.getInstance(getContext()).getGame(listener);
            }
        }, 0, 1000);
    }


    @Override
    public void onResponse(JSONObject response) {
        try {

            // There's no concept of rounds on the server in terms of how it is suppose to be used here
            //roundNumberTextView.setText(getString(R.string.round) + " " + round);

            boolean isFinished = response.getBoolean("isFinished");
            if(!isFinished){
                return;
            }

            gameScoreReceivedPollTimer.cancel();

            scores = response.getJSONArray("scores");

            // No need to build the tables if no changes happened
            tableLayout.removeAllViews();
            for (int i = 0; i < scores.length(); i++){
                int score = scores.optInt(i);
                TableRow row = new TableRow(getActivity());
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                        0, TableRow.LayoutParams.WRAP_CONTENT, 1f
                );
                row.setLayoutParams(layoutParams);

                TextView playerView = new TextView(getActivity());
                playerView.setText(getString(R.string.player) + " " + i);
                playerView.setTextColor(Color.BLACK);
                playerView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                playerView.setTextSize(25);
                playerView.setBackgroundColor(Color.WHITE);
                playerView.setGravity(Gravity.START);
                playerView.setPadding(3,3,3,0);
                row.addView(playerView);

                TextView scoreView = new TextView(getActivity());
                scoreView.setText(score + " " + getString(R.string.pt));
                scoreView.setTextColor(Color.BLACK);
                scoreView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                scoreView.setTextSize(25);
                scoreView.setBackgroundColor(Color.WHITE);
                scoreView.setGravity(Gravity.END);
                scoreView.setPadding(3,3,3,0);
                row.addView(scoreView);

                tableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            }


            GameState.getInstance().setWinners(findIndexOfWinners());
            exitButton.setClickable(true);
            exitButton.setAlpha(1f);
            exitButton.setEnabled(true);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.exitButton:
                onGoToView.goToExitView();
                break;
        }
    }

    private ArrayList<Integer> findIndexOfWinners(){

        int largestNumber = -1;
        for (int i = 0; i < scores.length(); i++){
            if (scores.optInt(i) > largestNumber){
                largestNumber = scores.optInt(i);
            }
        }

        ArrayList<Integer> indexOfWinners = new ArrayList<>();
        for (int i = 0; i < scores.length(); i++){
            try {
                int number = scores.getInt(i);
                if (number == largestNumber){
                    indexOfWinners.add(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return indexOfWinners;

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
