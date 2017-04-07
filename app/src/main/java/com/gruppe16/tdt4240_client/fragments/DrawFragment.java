package com.gruppe16.tdt4240_client.fragments;

import android.graphics.Bitmap;
import android.net.Uri;
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
import com.gruppe16.tdt4240_client.DrawingView;
import com.gruppe16.tdt4240_client.FragmentChanger;
import com.gruppe16.tdt4240_client.GameState;
import com.gruppe16.tdt4240_client.NetworkAbstraction;
import com.gruppe16.tdt4240_client.R;
import com.gruppe16.tdt4240_client.actions.ActionContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class DrawFragment extends Fragment {

    private DrawingView drawingView;
    private TextView timeLeftTextView;
    private TextView drawWord;
    private OnSubmitDrawingListener mListener;
    private ImageButton imageButton;
    private GameState gameState;

    public Bitmap finishedDrawing;


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

        pollForGame();

        //The countdown timer
        new CountDownTimer(5000, 1000) {
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
                GameState.getInstance().setImageString(imageString);

                NetworkAbstraction.getInstance(getContext()).submitDrawing(new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("success")){
                                gameState.setRound(gameState.getRound() + 1);
                                FragmentChanger.goToGuessView(getActivity());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();

        return rootView;
    }

    private void pollForGame(){
        NetworkAbstraction.getInstance(getActivity()).pollForGame(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (gameState.getRound() == 0){
                        JSONArray initialWords = response.getJSONArray("initialWords");
                        int id = Integer.parseInt(gameState.getMyPlayerId());
                        drawWord.setText(initialWords.get(id) + "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    interface OnSubmitDrawingListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
