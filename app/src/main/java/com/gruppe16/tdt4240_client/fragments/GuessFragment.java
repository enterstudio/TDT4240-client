package com.gruppe16.tdt4240_client.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gruppe16.tdt4240_client.GameState;
import com.gruppe16.tdt4240_client.NetworkAbstraction;
import com.gruppe16.tdt4240_client.R;
import com.gruppe16.tdt4240_client.interfaces.OnGoToView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Timer;
import java.util.TimerTask;

public class GuessFragment extends Fragment {

    private EditText guess;
    private TextView timeLeftTextView;
    private Button submitButton;
    private ImageView imageView;
    private boolean guessSent = false;
    private Timer GamePollingTimer;
    private OnGoToView onGoToView;
    private CountDownTimer countDownTimer;
    private boolean guessSubmitted = false;

    private Response.ErrorListener guessErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            guessSubmitted = false;
            error.printStackTrace();
        }
    };

    private Response.Listener drawingListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            if (isAdded()) {
                try {

                    // Start countdown when drawing is received
                    countDownTimer.start();

                    JSONObject image = response.getJSONObject("image");
                    String encodedImage = image.getString("file");

                    byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    imageView.setImageBitmap(decodedByte);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private Response.Listener gameListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            if (isAdded()) {
                try {
                    int id = Integer.parseInt(GameState.getInstance().getMyPlayerId());
                    int round = GameState.getInstance().getRound();
                    int numberOfPlayers = GameState.getInstance().getNumberOfPlayers();
                    int nextGuessBlockIndex = (id + round) % numberOfPlayers;
                    // We want to show the previous drawing
                    int previousDepth = GameState.getInstance().getGuessBlockDepth() - 1;

                    if (!(response.getJSONArray("guessBlocks").getJSONArray(nextGuessBlockIndex).isNull(previousDepth))) {
                        GamePollingTimer.cancel();
                        String drawingId = response.getJSONArray("guessBlocks").getJSONArray(nextGuessBlockIndex).getJSONObject(previousDepth).getString("drawingId");
                        GameState.getInstance().setDrawingId(drawingId);
                        NetworkAbstraction.getInstance(getActivity()).getDrawing(drawingListener);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private Response.Listener guessListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            if (isAdded()) {
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        if (GameState.getInstance().getRound() >= GameState.getInstance().getNumberOfPlayers() - 1) {
                            onGoToView.goToFinishedView();
                        } else {
                            GameState.getInstance().setRound(GameState.getInstance().getRound() + 1);
                            onGoToView.goToDrawView();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    // Required empty public constructor
    public GuessFragment() {}

    public static GuessFragment newInstance() {
        GuessFragment fragment = new GuessFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_draw, container, false);
        timeLeftTextView = (TextView) rootView.findViewById(R.id.timeLeftTextView);
        //guessField = (LinearLayout) rootView.findViewById(R.id.guessField);
        imageView = (ImageView) rootView.findViewById(R.id.imageReceived);
        submitButton = (Button) rootView.findViewById(R.id.submitButton);
        guess = (EditText) rootView.findViewById(R.id.guessWord);
        guess.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.VISIBLE);

        imageView.setVisibility(View.VISIBLE);
        guess.setInputType(InputType.TYPE_CLASS_TEXT);

        guess.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (guess.getText().length() > 0) {
                    submitButton.setBackgroundResource(R.drawable.button_bg_active);
                } else {
                    submitButton.setBackgroundResource(R.drawable.button_bg);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        // Start polling for game
        pollForGame();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!guessSent) {
                    sendGuess();
                    guessSent = true;
                    guess.setEnabled(false);
                    submitButton.setBackgroundResource(R.drawable.button_bg);
                }

            }
        });


        countDownTimer = new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeftTextView.setText(getString(R.string.seconds_left) + getString(R.string.semicolon) + " " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timeLeftTextView.setText(getString(R.string.seconds_left) + getString(R.string.semicolon) + " " + 0);
                submitButton.setOnClickListener(null);
                if (!guessSent) sendGuess();
            }
        };

        return rootView;
    }

    private void pollForGame() {
        GamePollingTimer = new Timer();
        GamePollingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                NetworkAbstraction.getInstance(getContext()).pollForGame(gameListener);
            }
        }, 0, 1000);
    }

    private void sendGuess() {
        String guessWord = guess.getText().toString();
        if (guessWord.isEmpty()) guessWord = guessWord + getString(R.string.no_answer);
        GameState.getInstance().setGuessWord(guessWord);
        if (!guessSubmitted) {
            guessSubmitted = true;
            NetworkAbstraction.getInstance(getContext()).submitGuess(guessListener, guessErrorListener);
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
