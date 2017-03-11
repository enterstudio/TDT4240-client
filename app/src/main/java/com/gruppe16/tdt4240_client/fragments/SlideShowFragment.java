package com.gruppe16.tdt4240_client.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gruppe16.tdt4240_client.R;

public class SlideShowFragment extends Fragment {

    private Button okButton;
    private Button declindeButton;

    public SlideShowFragment() {
        // Required empty public constructor
    }

    public static SlideShowFragment newInstance() {
        SlideShowFragment fragment = new SlideShowFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_slide_show, container, false);

        okButton = (Button) rootView.findViewById(R.id.okButton);
        declindeButton = (Button) rootView.findViewById(R.id.declinedButton);

        //SET WIDTH OF BUTTON HERE TO HALF SCREEN!!!

        return rootView;

    }



}
