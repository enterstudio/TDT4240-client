package com.gruppe16.tdt4240_client.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gruppe16.tdt4240_client.DrawingView;
import com.gruppe16.tdt4240_client.FragmentChanger;
import com.gruppe16.tdt4240_client.R;

public class DrawFragment extends Fragment {

    private DrawingView drawingView;
    private TextView timeLeftTextView;
    private OnSubmitDrawingListener mListener;
    private Button drawButton;
    private Button eraseButton;
    private Bitmap finishedDrawing;



    public DrawFragment() {
        // Required empty public constructor
    }

    public static DrawFragment newInstance() {
        DrawFragment fragment = new DrawFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_draw, container, false);
        drawingView = (DrawingView) rootView.findViewById(R.id.drawingView);
        timeLeftTextView = (TextView) rootView.findViewById(R.id.timeLeftTextView);
        drawButton = (Button) rootView.findViewById(R.id.drawButton);
        eraseButton = (Button) rootView.findViewById(R.id.eraseButton);

        //Erase and draw buttons functionality
        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.mPaint.setColor(Color.BLACK);
                drawingView.mPaint.setStrokeWidth(12);
            }
        });

        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.mPaint.setColor(Color.WHITE);
                drawingView.mPaint.setStrokeWidth(80);
            }
        });


        //The countdown timer
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeftTextView.setText("Seconds left: " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                timeLeftTextView.setText("Seconds left: 0");
                finishedDrawing = drawingView.getFinishedDrawing();
                //someImageView.setImageBitmap(finishedDrawing);
                // TODO: kall en eller annen funksjon som sender bilde object og bytter skjermbilde

            }
        }.start();

        return rootView;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSubmitDrawingListener) {
            mListener = (OnSubmitDrawingListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
    public interface OnSubmitDrawingListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}