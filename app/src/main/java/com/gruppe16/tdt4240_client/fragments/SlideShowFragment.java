package com.gruppe16.tdt4240_client.fragments;

import android.content.Context;
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
import com.gruppe16.tdt4240_client.GameState;
import com.gruppe16.tdt4240_client.NetworkAbstraction;
import com.gruppe16.tdt4240_client.R;
import com.gruppe16.tdt4240_client.interfaces.OnGoToView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class SlideShowFragment extends Fragment {

    private TextView initialWordView;
    private TextView guessPlayerIdView;
    private TextView guessValueView;
    private ImageView drawingView;
    private TextView drawerIdView;
    private JSONArray guessBlock;
    private OnGoToView onGoToView;
    private int currentGuessIndex;
    private int guessBlockLength;
    private Map<String, Integer> scores;

    private Response.Listener submitListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                System.out.println("Submit response:");
                System.out.println(response.toString(2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Response.Listener gameListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            if(isAdded()){
                try {
                    // Get the array of players
                    JSONArray playerList = response.getJSONArray("players");

                    // Update the scores with the correct number of players
                    for (int i = 0; i < playerList.length(); i++){
                       scores.put(playerList.get(i).toString(), 0);
                    }

                    // Get the initial word
                    JSONArray initialWordList = response.getJSONArray("initialWords");

                    // Get the initial word of the current player
                    int indexOfInitialWord = (Integer.parseInt(GameState.getInstance().getMyPlayerId()));
                    String initialWord = initialWordList.get(indexOfInitialWord).toString();
                    // Display the initial word
                    initialWordView.setText(initialWord);

                    // Get the guessblock object
                    JSONArray guessBlocks = response.getJSONArray("guessBlocks");
                    // Get the guessblock array of the inital word
                    guessBlock = guessBlocks.getJSONArray(indexOfInitialWord);
                    // Update the length of the guessBlock
                    guessBlockLength = guessBlock.length();

                    // Get the current guess from the array in the guessblock
                    JSONObject guess = guessBlock.getJSONObject(currentGuessIndex);

                    // Display the id of the drawer
                    String drawerId = guess.getString("drawerId");
                    drawerIdView.setText(getString(R.string.player) + " " + drawerId + " " + getString(R.string.drew));
                    // Display the drawing
                    String drawingId = guess.getString("drawingId");
                    GameState.getInstance().setDrawingId(drawingId);
                    NetworkAbstraction.getInstance(getActivity()).getDrawing(drawingListener);

                    // Display the id of the guesser
                    String guesserId = guess.getString("guesserId");
                    guessPlayerIdView.setText(getString(R.string.player) + " " + guesserId + " " +  getString(R.string.guessed));

                    // Display the guess value
                    String guessValue = guess.getString("guess");
                    guessValueView.setText(guessValue);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private Response.Listener drawingListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            if(isAdded()) {
                try {

                    JSONObject image = response.getJSONObject("image");
                    String encodedImage = image.getString("file");

                    byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    int height = drawingView.getHeight();
                    int width = drawingView.getWidth();

                    Bitmap scaledBitmap = decodedByte.createScaledBitmap(decodedByte, width, height, true);

                    drawingView.setImageBitmap(scaledBitmap);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    // Required empty public constructor
    public SlideShowFragment() {}

    public static SlideShowFragment newInstance() {
        return new SlideShowFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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

        // Create a new HashMap
        scores = new HashMap<>();

        // set current guess index to 0
        currentGuessIndex = 0;

        // poll for updated game information
        NetworkAbstraction.getInstance(getContext()).pollForGame(gameListener);

        return rootView;

    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.okButton:
                    try {
                        // Find the current guess
                        JSONObject guess = guessBlock.getJSONObject(currentGuessIndex);
                        // Find the id of the drawer
                        String drawerId = guess.getString("drawerId");
                        // Find the id of the guesser
                        String guesserId = guess.getString("guesserId");

                        // Update the score for the drawer and the guesser
                        scores.put(drawerId, scores.get(drawerId) + 1 );
                        scores.put(guesserId, scores.get(guesserId) + 1 );

                        if (currentGuessIndex + 1 >= guessBlockLength) {
                            onGoToView.goToScoreboardView();
                            GameState.getInstance().setScores(scores);
                            NetworkAbstraction.getInstance(getActivity()).submitScore(submitListener);
                        }
                        else {
                            // increment the current guess index
                            currentGuessIndex++;

                            guess = guessBlock.getJSONObject(currentGuessIndex);

                            // Display the drawer ID
                            drawerId = guess.getString("drawerId");
                            drawerIdView.setText(getString(R.string.player) + getString(R.string.semicolon) + " " + drawerId + " " + getString(R.string.drew));

                            // Display the drawing
                            String drawingId = guess.getString("drawingId");
                            GameState.getInstance().setDrawingId(drawingId);
                            NetworkAbstraction.getInstance(getActivity()).getDrawing(drawingListener);

                            // Display the guesser ID
                            guesserId = guess.getString("guesserId");
                            guessPlayerIdView.setText(getString(R.string.player) + getString(R.string.semicolon) + " " + guesserId + " " +  getString(R.string.guessed));

                            // Display the guess value
                            String guessValue = guess.getString("guess");
                            guessValueView.setText(guessValue);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case R.id.declinedButton:
                    try {

                        if (currentGuessIndex + 1 >= guessBlockLength){
                            onGoToView.goToScoreboardView();
                            GameState.getInstance().setScores(scores);
                            NetworkAbstraction.getInstance(getActivity()).submitScore(submitListener);
                        }
                        else {
                            // increment the current guess index
                            currentGuessIndex++;

                            // Get the current guess from the array in the guessblock
                            JSONObject guess = guessBlock.getJSONObject(currentGuessIndex);

                            // Display the id of the drawer
                            String drawerId = guess.getString("drawerId");
                            drawerIdView.setText(getString(R.string.player) + getString(R.string.semicolon) + " " + drawerId + " " + getString(R.string.drew));

                            // Display the drawing
                            String drawingId = guess.getString("drawingId");
                            GameState.getInstance().setDrawingId(drawingId);
                            NetworkAbstraction.getInstance(getActivity()).getDrawing(drawingListener);

                            // Display the id of the guesser
                            String guesserId = guess.getString("guesserId");
                            guessPlayerIdView.setText(getString(R.string.player) + getString(R.string.semicolon) + " " + guesserId + " " +  getString(R.string.guessed));

                            // Display the guess value
                            String guessValue = guess.getString("guess");
                            guessValueView.setText(guessValue);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                break;
            }
        }
    };

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

