package com.gruppe16.tdt4240_client.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gruppe16.tdt4240_client.GameState;
import com.gruppe16.tdt4240_client.R;
import com.gruppe16.tdt4240_client.interfaces.OnGoToView;

import java.util.ArrayList;


public class ExitFragment extends Fragment {

    private OnGoToView onGoToView;

    private Button.OnClickListener mainMenuButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Update the variables that won't be updated
            // just by playing the game
            GameState.getInstance().setRound(0);
            GameState.getInstance().setGuessBlockDepth(0);
            onGoToView.goToMainMenu();
        }
    };

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
        Button button = (Button) rootView.findViewById(R.id.main_menu_button);

        ArrayList<Integer> winners = GameState.getInstance().getWinners();

        assert winners != null;
        if (winners.size() == 0){
            playerWonTextView.setText(getString(R.string.player) + " " + winners.get(0) + " " + getString(R.string.won_the_game));
        }
        else {
            String winnerText = "";
            for (int winner : winners){
                winnerText += winner + ", ";
            }
            winnerText = winnerText.replaceAll(", $", "");
            playerWonTextView.setText(getString(R.string.players) + " " + winnerText + " " + getString(R.string.won_the_game));
        }

        button.setOnClickListener(mainMenuButtonListener);

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
