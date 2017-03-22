package com.gruppe16.tdt4240_client.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.Response;
import com.gruppe16.tdt4240_client.FragmentChanger;
import com.gruppe16.tdt4240_client.NetworkAbstraction;
import com.gruppe16.tdt4240_client.R;
import org.json.JSONObject;
import java.util.Timer;
import java.util.TimerTask;


public class WaitingFragment extends Fragment implements Response.Listener<JSONObject> {

    private String gamePin;

    public WaitingFragment() {
        // Required empty public constructor
    }

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
        gamePin = this.getArguments().getString("gamePin");
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
        System.out.println(response);
        try{
            boolean isStarted = (boolean) response.get("isStarted");
            String gamePin = (String) response.get("gamePin");
            String playerID = (String) response.get("playerID");
            if (isStarted){
                FragmentChanger.goToDrawView(getActivity(), gamePin, playerID);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
