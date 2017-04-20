package com.gruppe16.tdt4240_client.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Response;
import com.gruppe16.tdt4240_client.GameState;
import com.gruppe16.tdt4240_client.NetworkAbstraction;
import com.gruppe16.tdt4240_client.R;
import com.gruppe16.tdt4240_client.FragmentChanger;
import com.gruppe16.tdt4240_client.interfaces.OnGoToView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


public class FinishedFragment extends Fragment {

    private static String TAG = "FinishedFragment";

    private Button startSlideshowButton;
    private OnGoToView onGoToView;
    private Timer pollTimer;

    private Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try{
                int round = (Integer) response.get("round");
                int numberOfPlayers = GameState.getInstance().getNumberOfPlayers();
                if(round == (numberOfPlayers * 2) - 1 ){
                    startSlideshowButton.setClickable(true);
                    startSlideshowButton.setAlpha(1f);
                    pollTimer.cancel();
                }
            }
            catch (JSONException e){
                Log.d(TAG, e.toString());
                return;
            }
        }
    };

    // Required empty public constructor
    public FinishedFragment() {}

    public static FinishedFragment newInstance() {
        FinishedFragment fragment = new FinishedFragment();
        return fragment;
    }

    public void pollForAllDataReceived(){
        pollTimer = new Timer();
        pollTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                NetworkAbstraction.getInstance(getContext()).getGame(listener);
            }
        }, 0, 1000);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_finishedfragment, container, false);
        startSlideshowButton = (Button) rootView.findViewById(R.id.startSlideshowButton);
        startSlideshowButton.setClickable(false);
        startSlideshowButton.setAlpha(0.5f);
        startSlideshowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoToView.goToSlideShowView();
            }
        });
        return rootView;
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



