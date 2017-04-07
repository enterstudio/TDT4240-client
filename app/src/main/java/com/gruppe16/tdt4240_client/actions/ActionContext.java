package com.gruppe16.tdt4240_client.actions;

import android.widget.ImageButton;
import com.gruppe16.tdt4240_client.DrawingView;
import com.gruppe16.tdt4240_client.interfaces.ActionState;

/**
 * Created by Thomas on 06.04.2017.
 */

public class ActionContext {

    private static volatile ActionContext instance = null;

    private ActionContext() {}

    private ActionState state;
    private DrawingView view;
    private ImageButton imageButton;

    public void initializeState(){
        setState(new DrawState());
    }

    public void setDrawingView(DrawingView view){
        this.view = view;
    }

    public void setImageButton(ImageButton imageButton){
        this.imageButton = imageButton;
    }

    private void setState(final ActionState state){
        this.state = state;
    }

    public void doAction(){
        if (state.getClass() == DrawState.class) setState(new EraseState());
        else setState(new DrawState());
        state.doAction(view, imageButton);
    }

    public static ActionContext getInstance() {
        if (instance == null) {
            synchronized(ActionContext.class) {
                if (instance == null) {
                    instance = new ActionContext();
                }
            }
        }
        return instance;
    }


}
