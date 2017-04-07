package com.gruppe16.tdt4240_client.actions;

import android.graphics.Color;
import android.widget.ImageButton;
import com.gruppe16.tdt4240_client.DrawingView;
import com.gruppe16.tdt4240_client.R;
import com.gruppe16.tdt4240_client.interfaces.ActionState;

/**
 * Created by Thomas on 06.04.2017.
 */

public class EraseState implements ActionState {
    @Override
    public void doAction(DrawingView view, ImageButton imageButton) {
        view.mPaint.setColor(Color.WHITE);
        view.mPaint.setStrokeWidth(80);
        imageButton.setImageResource(R.drawable.eraser);
    }
}
