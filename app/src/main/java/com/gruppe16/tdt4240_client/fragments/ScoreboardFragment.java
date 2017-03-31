package com.gruppe16.tdt4240_client.fragments;

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
import com.gruppe16.tdt4240_client.FragmentChanger;
import com.gruppe16.tdt4240_client.NetworkAbstraction;
import com.gruppe16.tdt4240_client.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class ScoreboardFragment extends Fragment implements Response.Listener<JSONObject>, View.OnClickListener {

    private TextView roundNumberTextView;
    private Button playAnotherRoundButton;
    private Button exitButton;
    private TableLayout tableLayout;

    private String gamePin;
    private String myPlayerId;
    private JSONArray scoreArray;

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

        roundNumberTextView = (TextView) rootView.findViewById(R.id.roundNumberTextView);
        TextView playerNumberTextView = (TextView) rootView.findViewById(R.id.playerNumberTextView);
        tableLayout = (TableLayout) rootView.findViewById(R.id.scoreboard);

        gamePin = this.getArguments().getString("gamePin");
        myPlayerId = this.getArguments().getString("myPlayerId");
        playerNumberTextView.setText(getString(R.string.player) + " " + myPlayerId);

        exitButton = (Button) rootView.findViewById(R.id.exitButton);
        exitButton.setClickable(false);
        if (myPlayerId.equals("0")) {
            exitButton.setAlpha(0.3f);
            exitButton.setOnClickListener(this);
            exitButton.setVisibility(View.VISIBLE);
        }
        else {
            exitButton.setVisibility(View.INVISIBLE);
        }


        playAnotherRoundButton = (Button) rootView.findViewById(R.id.playAnotherRoundButton);
        playAnotherRoundButton.setClickable(false);
        if (myPlayerId.equals("0")) {
            playAnotherRoundButton.setAlpha(0.3f);
            playAnotherRoundButton.setOnClickListener(this);
            playAnotherRoundButton.setVisibility(View.VISIBLE);
        }
        else {
            playAnotherRoundButton.setVisibility(View.INVISIBLE);
        }

        roundNumberTextView.setText(getString(R.string.round));

        setPollingForGameStart();

        return rootView;
    }


    private void setPollingForGameStart(){
        Timer gameStartPollTimer = new Timer();
        final Response.Listener<JSONObject> listener = this;
        gameStartPollTimer.scheduleAtFixedRate( new TimerTask() {
            @Override
            public void run() {
                NetworkAbstraction.getInstance(getContext()).pollForGame(gamePin, listener);
            }
        }, 0, 1000);
    }


    @Override
    public void onResponse(JSONObject response) {
        try {
            JSONObject game = response.getJSONObject("game");

            String round = game.getString("round");
            roundNumberTextView.setText(getString(R.string.round) + " " + round);

            boolean isFinished = (boolean) game.get("isFinished");

            scoreArray = game.getJSONArray("scores");
            JSONArray oldScoreArray = new JSONArray();

            // No need to build the tables if no changes happened
            if (!(oldScoreArray.equals(scoreArray))){

                for (int i = 0; i < scoreArray.length(); i++){
                    int score = scoreArray.optInt(i);
                    TableRow row = new TableRow(getActivity());
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(
                            0, TableRow.LayoutParams.WRAP_CONTENT, 1f
                    );
                    row.setLayoutParams(lp);

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

            }

            if (isFinished){
                exitButton.setClickable(true);
                exitButton.setAlpha(1f);
                playAnotherRoundButton.setClickable(true);
                playAnotherRoundButton.setAlpha(1f);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.exitButton:
                ArrayList<Integer> winners = findIndexOfWinners();
                FragmentChanger.goToExitView(myPlayerId, gamePin, winners, getActivity());
                break;
            case R.id.playAnotherRoundButton:
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setMessage("To be developed...");
                alertDialog.show();
                break;
        }
    }

    private ArrayList<Integer> findIndexOfWinners(){

        int largestNumber = -1;
        for (int i = 0; i < scoreArray.length(); i++){
            if (scoreArray.optInt(i) > largestNumber){
                largestNumber = scoreArray.optInt(i);
            }
        }

        ArrayList<Integer> indexOfWinners = new ArrayList<>();
        for (int i = 0; i < scoreArray.length(); i++){
            try {
                int number = scoreArray.getInt(i);
                if (number == largestNumber){
                    indexOfWinners.add(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return indexOfWinners;

    }




}
