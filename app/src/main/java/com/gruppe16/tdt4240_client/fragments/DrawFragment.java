package com.gruppe16.tdt4240_client.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gruppe16.tdt4240_client.DrawingView;
import com.gruppe16.tdt4240_client.GameState;
import com.gruppe16.tdt4240_client.NetworkAbstraction;
import com.gruppe16.tdt4240_client.R;
import com.gruppe16.tdt4240_client.actions.ActionContext;
import com.gruppe16.tdt4240_client.interfaces.OnGoToView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class DrawFragment extends Fragment {

    private DrawingView drawingView;
    private TextView timeLeftTextView;
    private TextView drawWord;
    private ImageButton imageButton;
    private GameState gameState;
    private Timer gamePollingTimer;
    private Bitmap finishedDrawing;
    private OnGoToView onGoToView;
    private boolean drawingSubmitted = false;

    private Response.ErrorListener submitDrawingErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            drawingSubmitted = false;
            error.printStackTrace();
        }
    };

    private Response.Listener<JSONObject> gamePollListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            if (isAdded()) {
                try {
                    countDownTimer.start();
                    if (gameState.getRound() == 0) {
                        gamePollingTimer.cancel();
                        JSONArray initialWords = response.getJSONArray("initialWords");
                        int id = Integer.parseInt(gameState.getMyPlayerId());
                        drawWord.setText(initialWords.get(id) + "");
                    } else {

                        int id = Integer.parseInt(GameState.getInstance().getMyPlayerId());
                        int round = GameState.getInstance().getRound();
                        int numberOfPlayers = GameState.getInstance().getNumberOfPlayers();
                        int nextGuessBlockIndex = (id + round) % numberOfPlayers;
                        // We want to show the previous drawing
                        int previousDepth = GameState.getInstance().getGuessBlockDepth() - 1;

                        if (!(response.getJSONArray("guessBlocks").getJSONArray(nextGuessBlockIndex).getJSONObject(previousDepth).getString("guess").equals("null"))) {
                            gamePollingTimer.cancel();
                            String guessWord = response.getJSONArray("guessBlocks").getJSONArray(nextGuessBlockIndex).getJSONObject(previousDepth).getString("guess");
                            drawWord.setText(guessWord + "");
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private Response.Listener<JSONObject> submitDrawingListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            if(isAdded()) {
                try {
                    if (response.getString("status").equals("success")) {
                        GameState.getInstance().setRound(GameState.getInstance().getRound() + 1);
                        GameState.getInstance().setGuessBlockDepth(GameState.getInstance().getGuessBlockDepth() + 1);
                        onGoToView.goToGuessView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private CountDownTimer countDownTimer;


    // Required empty public constructor
    public DrawFragment() {}

    public static DrawFragment newInstance() {
        DrawFragment fragment = new DrawFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_draw, container, false);
        drawingView = (DrawingView) rootView.findViewById(R.id.drawingView);
        timeLeftTextView = (TextView) rootView.findViewById(R.id.timeLeftTextView);
        imageButton = (ImageButton) rootView.findViewById(R.id.actionButton);
        drawWord = (TextView) rootView.findViewById(R.id.drawWord);

        gameState = GameState.getInstance();

        rootView.findViewById(R.id.drawWord).setVisibility(View.VISIBLE);
        drawingView.setVisibility(View.VISIBLE);
        imageButton.setVisibility(View.VISIBLE);
        imageButton.setActivated(true);

        final ActionContext actionContext = ActionContext.getInstance();
        actionContext.initializeState();
        actionContext.setDrawingView(drawingView);
        actionContext.setImageButton(imageButton);

        final Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButton.startAnimation(shake);
                actionContext.doAction();
            }
        });

        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeftTextView.setText(getString(R.string.seconds_left) + getString(R.string.semicolon) + " " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                timeLeftTextView.setText(getString(R.string.seconds_left) + getString(R.string.semicolon) + " " + 0);
                drawingView.stopDraw();
                finishedDrawing = drawingView.getFinishedDrawing();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                finishedDrawing.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
                byte [] byte_arr = stream.toByteArray();
                String imageString = Base64.encodeToString(byte_arr, Base64.DEFAULT);

                // Update the game state with the compressed image string
                gameState.setImageString(imageString);

                if (!drawingSubmitted) {
                    drawingSubmitted = true;
                    NetworkAbstraction.getInstance(getContext()).submitDrawing(submitDrawingListener, submitDrawingErrorListener);
                }

            }
        };

        pollForGame();

        return rootView;
    }

    private void pollForGame(){
        gamePollingTimer = new Timer();
        gamePollingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                NetworkAbstraction.getInstance(getContext()).getGame(gamePollListener);
            }
        }, 0, 1000);
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
