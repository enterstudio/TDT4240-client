package com.gruppe16.tdt4240_client.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.Response;
import com.gruppe16.tdt4240_client.GameState;
import com.gruppe16.tdt4240_client.NetworkAbstraction;
import com.gruppe16.tdt4240_client.R;
import com.gruppe16.tdt4240_client.interfaces.OnGoToView;
import org.json.JSONObject;
import java.util.Timer;
import java.util.TimerTask;


public class WaitingFragment extends Fragment {

    private Timer gameStartPollTimer;
    private OnGoToView onGoToView;

    private Response.Listener<JSONObject> pollForGameListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            if (isAdded()){
                try{
                    boolean isStarted = (boolean) response.get("isStarted");
                    if (isStarted){
                        gameStartPollTimer.cancel();
                        int numberOfPlayers = response.getJSONArray("players").length();
                        GameState.getInstance().setNumberOfPlayers(numberOfPlayers);
                        onGoToView.goToDrawView();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    };

    // Required empty public constructor
    public WaitingFragment() {}

    public static WaitingFragment newInstance() {
        return new WaitingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_waiting, container, false);
        setPollingForGameStart();
        return rootView;
    }

    private void setPollingForGameStart(){
        gameStartPollTimer = new Timer();
        gameStartPollTimer.scheduleAtFixedRate( new TimerTask() {
            @Override
            public void run() {
                NetworkAbstraction.getInstance(getContext()).pollForGame(pollForGameListener);
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
