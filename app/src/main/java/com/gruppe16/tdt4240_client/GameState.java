package com.gruppe16.tdt4240_client;

import android.app.Application;

/**
 * Created by Thomas on 06.04.2017.
 */

public class GameState extends Application {

    private static volatile GameState instance = null;
    private String gamePin;
    private String myPlayerId;
    private String imageString;
    private String drawingId;
    private int round = 0;

    private GameState(){}

    public static GameState getInstance() {
        if (instance == null) {
            synchronized(GameState.class) {
                if (instance == null) {
                    instance = new GameState();
                }
            }
        }
        return instance;
    }

    public String getGamePin() {
        return gamePin;
    }

    public void setGamePin(String gamePin) {
        this.gamePin = gamePin;
    }

    public String getMyPlayerId() {
        return myPlayerId;
    }

    public void setMyPlayerId(String myPlayerId) {
        this.myPlayerId = myPlayerId;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public String getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(String drawingId) {
        this.drawingId = drawingId;
    }
}
