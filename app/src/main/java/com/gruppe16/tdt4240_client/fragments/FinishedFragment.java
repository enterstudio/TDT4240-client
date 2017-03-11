package com.gruppe16.tdt4240_client.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gruppe16.tdt4240_client.R;
import com.gruppe16.tdt4240_client.FragmentChanger;


public class FinishedFragment extends Fragment {

    private Button startSlideshowButton;


    public FinishedFragment() {
        // Required empty public constructor
    }

    public static FinishedFragment newInstance() {
        FinishedFragment fragment = new FinishedFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_finishedfragment, container, false);
        startSlideshowButton = (Button) rootView.findViewById(R.id.startSlideshowButton);

        startSlideshowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentChanger.goToSlideshowView(getActivity());
            }


    });
        return rootView;
    }}

