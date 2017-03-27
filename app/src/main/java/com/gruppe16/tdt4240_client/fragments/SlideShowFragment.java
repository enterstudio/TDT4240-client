package com.gruppe16.tdt4240_client.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Response;
import com.gruppe16.tdt4240_client.FragmentChanger;
import com.gruppe16.tdt4240_client.NetworkAbstraction;
import com.gruppe16.tdt4240_client.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SlideShowFragment extends Fragment {

    private TextView initialWordView;
    private TextView guessPlayerIdView;
    private TextView guessValueView;
    private ImageView drawingView;
    private TextView drawerIdView;
    private JSONArray guessBlock;
    private String gamePin;
    private String myPlayerId;
    private int currentGuessIndex;
    private int guessBlockLength;

    public SlideShowFragment() {
        // Required empty public constructor
    }

    public static SlideShowFragment newInstance() {
        return new SlideShowFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_slide_show, container, false);

        initialWordView = (TextView) rootView.findViewById(R.id.initialWordView);
        guessPlayerIdView = (TextView) rootView.findViewById(R.id.guessPlayerIdView);
        guessValueView = (TextView) rootView.findViewById(R.id.guessValueView);

        drawerIdView = (TextView) rootView.findViewById(R.id.drawerIdView);
        drawingView = (ImageView) rootView.findViewById(R.id.drawingView);

        Button okButton = (Button) rootView.findViewById(R.id.okButton);
        okButton.setOnClickListener(onClickListener);
        Button declineButton = (Button) rootView.findViewById(R.id.declinedButton);
        declineButton.setOnClickListener(onClickListener);

        // Get the gamePin value from arguments
        gamePin = getArguments().getString("gamePin");
        myPlayerId = getArguments().getString("myPlayerId");

        // set current guess index to 0
        currentGuessIndex = 0;

        // poll for updated game information
        getGameFromServer();

        return rootView;

    }

    /**
     * Gets the most recent game data from the server
     */
    private void getGameFromServer(){
        Response.Listener<JSONObject> gameListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Get the game object itself.
                try {
                    JSONObject game = response.getJSONObject("game");
                    // Get the array of players
                    // TODO: playerList is not used
                    JSONArray playerList = game.getJSONArray("players");
                    // Get the initial word
                    JSONArray initialWordList = game.getJSONArray("initialWord");

                    // Get the initial word of the current player
                    int index = (Integer.parseInt(myPlayerId)) - 1;
                    String initialWord = initialWordList.get(index).toString();
                    // Display the initial word
                    initialWordView.setText(initialWord);

                    // Get the guessblock object
                    JSONObject guessBlocks = game.getJSONObject("guessBlock");
                    // Get the guessblock array of the inital word
                    guessBlock = guessBlocks.getJSONArray(initialWord);
                    // Update the length of the guessBlock
                    guessBlockLength = guessBlock.length();
                    // Get the current guess from the array in the guessblock
                    JSONObject guess = (JSONObject) guessBlock.get(currentGuessIndex);

                    // Display the id of the drawer
                    String drawerId = guess.getString("drawerId");
                    drawerIdView.setText(drawerId);

                    // Display the drawing
                    String drawingId = guess.getString("drawingId");
                    getDrawingFromServer(drawingId);

                    // Display the id of the guesser
                    String guesserId = guess.getString("guesserId");
                    guessPlayerIdView.setText(guesserId);

                    // Display the guess value
                    String guessValue = guess.getString("guessValue");
                    guessValueView.setText(guessValue);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        NetworkAbstraction.getInstance(getContext()).pollForGame(gamePin, gameListener);
    }

    /**
     * Gets the drawing with a specific id from the server
     */
    private void getDrawingFromServer(String drawingId){
        Response.Listener<JSONObject> drawingListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String drawing = response.getString("drawing");
                    byte[] decodedString = Base64.decode(drawing, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    drawingView.setImageBitmap(decodedByte);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        // TODO: Fix the getDrawing method in NetworkAbstraction
        NetworkAbstraction.getInstance(getContext()).getDrawing(drawingId, drawingListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.okButton:
                    try {

                        // TODO: Update the server when answer is accepted

                        if (currentGuessIndex++ >= guessBlockLength) {
                            FragmentChanger.goToScoreboardView(myPlayerId, getActivity());
                        }
                        else {
                            // increment the current guess index
                            currentGuessIndex++;

                            // Find the current guess
                            JSONObject guess = guessBlock.getJSONObject(currentGuessIndex);

                            // Display the id of the drawer
                            String drawerId = guess.getString("drawerId");
                            drawerIdView.setText(drawerId);

                            // Display the drawing
                            String drawingId = guess.getString("drawingId");
                            getDrawingFromServer(drawingId);

                            // Display the id of the guesser
                            String guesserId = guess.getString("guesserId");
                            guessPlayerIdView.setText(guesserId);

                            // Display the guess value
                            String guessValue = guess.getString("guessValue");
                            guessValueView.setText(guessValue);
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case R.id.declinedButton:
                    try {
                        // TODO: Update the server when answer is declined

                        if (currentGuessIndex++ >= guessBlockLength){
                            FragmentChanger.goToScoreboardView(myPlayerId, getActivity());
                        }
                        else {
                            // increment the current guess index
                            currentGuessIndex++;

                            // Find the current guess
                            JSONObject guess = guessBlock.getJSONObject(currentGuessIndex);

                            // Display the id of the drawer
                            String drawerId = guess.getString("drawerId");
                            drawerIdView.setText(drawerId);

                            // Display the drawing
                            String drawingId = guess.getString("drawingId");
                            getDrawingFromServer(drawingId);

                            // Display the id of the guesser
                            String guesserId = guess.getString("guesserId");
                            guessPlayerIdView.setText(guesserId);

                            // Display the guess value
                            String guessValue = guess.getString("guessValue");
                            guessValueView.setText(guessValue);
                        }

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                break;
            }
        }
    };

}

