package com.gruppe16.tdt4240_client.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.gruppe16.tdt4240_client.FragmentChanger;
import com.gruppe16.tdt4240_client.NetworkAbstraction;
import com.gruppe16.tdt4240_client.R;

import org.json.JSONObject;

public class GuessFragment extends Fragment {

    private LinearLayout guessField;
    private EditText guess;
    private TextView timeLeftTextView;
    private Button submitButton;
    private GuessFragment.OnSubmitGuessListener mListener;
    private ImageView imageView;
    private boolean guessSent = false;
    private String gamepin;
    private String playerId;

    public GuessFragment() {
        // Required empty public constructor
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        gamepin = getArguments().getString("gamepin");
        playerId = "2"; //TODO: get real PlayerId;

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_draw, container, false);
        timeLeftTextView = (TextView) rootView.findViewById(R.id.timeLeftTextView);
        guessField = (LinearLayout) rootView.findViewById(R.id.guessField);
        imageView = (ImageView) rootView.findViewById(R.id.imageReceived);
        submitButton = (Button) rootView.findViewById(R.id.submitButton);
        guess = (EditText) rootView.findViewById(R.id.guessWord);

        imageView.setVisibility(View.VISIBLE);
        guessField.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.VISIBLE);

        guess.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(guess.getText().length() > 0){
                    submitButton.setBackgroundResource(R.drawable.checkmark);
                }
                else{
                    submitButton.setBackgroundResource(R.drawable.checkmark_grey);
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
                }

            }
        });

        //The countdown timer
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeftTextView.setText("Seconds left: " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                timeLeftTextView.setText("Seconds left: 0");
                submitButton.setOnClickListener(null);

                if(!guessSent){
                    sendGuess();
                }
                FragmentChanger fc = new FragmentChanger();
                fc.goToDrawView(getActivity(), gamepin, playerId);
            }
        }.start();

        return rootView;
    }

    private void sendGuess(){
        String guessedWord = guess.getText().toString();
        NetworkAbstraction.getInstance(getContext()).submitGuess(gamepin, playerId, guessedWord, new Response.Listener<JSONObject>() {

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSubmitGuessListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
