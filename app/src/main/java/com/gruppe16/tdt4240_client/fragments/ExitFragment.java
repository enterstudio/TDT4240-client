package com.gruppe16.tdt4240_client.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gruppe16.tdt4240_client.R;

import java.util.ArrayList;


public class ExitFragment extends Fragment {

    // Required empty public constructor
    public ExitFragment() {}

    public static ExitFragment newInstance() {
        return new ExitFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exit, container, false);
        TextView playerWonTextView = (TextView) rootView.findViewById(R.id.playerWonTextView);

        ArrayList<Integer> winners = this.getArguments().getIntegerArrayList("winners");

        assert winners != null;
        if (winners.size() == 0){
            playerWonTextView.setText(getString(R.string.player) + " " + winners.get(0) + "has won!");
        }
        else {
            String winnerText = "";
            for (int winner : winners){
                winnerText += winner + ", ";
            }
            winnerText = winnerText.replaceAll(", $", "");
            playerWonTextView.setText(getString(R.string.players) + " " + winnerText + " has won!");
        }

        return rootView;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
