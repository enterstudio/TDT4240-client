package com.gruppe16.tdt4240_client.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.gruppe16.tdt4240_client.FragmentChanger;
import com.gruppe16.tdt4240_client.GameState;
import com.gruppe16.tdt4240_client.NetworkAbstraction;
import com.gruppe16.tdt4240_client.R;

import org.json.JSONException;
import org.json.JSONObject;

public class GuessFragment extends Fragment {

    private EditText guess;
    private TextView timeLeftTextView;
    private Button submitButton;
    private GuessFragment.OnSubmitGuessListener mListener;
    private ImageView imageView;
    private boolean guessSent = false;

    private Response.Listener drawingListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                System.out.println("Poll for drawing response: ");
                System.out.println(response.toString(2));

                JSONObject image = response.getJSONObject("image");
                String encodedImage = image.getString("file");

                byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                imageView.setImageBitmap(decodedByte);

                System.out.println("Round: " + GameState.getInstance().getRound());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Response.Listener pageListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                System.out.println("Guess response: ");
                System.out.println(response.toString(2));
                String drawingId = response.getJSONArray("guessBlocks").getJSONArray(0).getJSONObject(0).getString("drawingId");
                GameState.getInstance().setDrawingId(drawingId);
                NetworkAbstraction.getInstance(getActivity()).getDrawing(drawingListener);
            } catch (JSONException e) {
                e.printStackTrace();
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
        
        NetworkAbstraction.getInstance(getContext()).getPage(pageListener);

        imageView.setVisibility(View.VISIBLE);
        guess.setInputType(InputType.TYPE_CLASS_TEXT);

        guess.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(guess.getText().length() > 0){
                    submitButton.setBackgroundResource(R.drawable.button_bg_active);
                }
                else{
                    submitButton.setBackgroundResource(R.drawable.button_bg);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!guessSent) {
                    sendGuess();
                    guessSent = true;
                    guess.setEnabled(false);
                    submitButton.setBackgroundResource(R.drawable.button_bg);
                }

            }
        });

        //The countdown timer
        new CountDownTimer(3000000, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeftTextView.setText(getString(R.string.seconds_left) + getString(R.string.semicolon) + " " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                timeLeftTextView.setText(getString(R.string.seconds_left) + getString(R.string.semicolon) + " " + 0);
                submitButton.setOnClickListener(null);

                if(!guessSent){
                    sendGuess();
                }
                FragmentChanger fc = new FragmentChanger();
                fc.goToDrawView(getActivity());
            }
        }.start();

        return rootView;
    }




    private void sendGuess(){

        // Set guess word to sharedPref
        String guessedWord = guess.getText().toString();
        NetworkAbstraction.getInstance(getContext()).submitGuess(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("SvarGuess:" + response);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSubmitGuessListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
