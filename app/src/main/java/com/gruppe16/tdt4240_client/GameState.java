package com.gruppe16.tdt4240_client;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thomas on 06.04.2017.
 */

public class GameState extends Application {

    private static volatile GameState instance = null;
    private String gamePin;
    private String myPlayerId;
    private String imageString;
    private String drawingId;
    private String guessWord;
    private int numberOfPlayers;
    private int round = 0;
    private int guessBlockDepth = 0;
    private Map<String, Integer> scores = new HashMap<>();
    private ArrayList<Integer> winners;

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

    public void setGuessWord(String guessWord) {
        this.guessWord = guessWord;
    }

    public String getGuessWord() {
        return guessWord;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public void setScores(Map<String, Integer> scores) {
        this.scores = scores;
    }

    public int getGuessBlockDepth() {
        return guessBlockDepth;
    }

    public void setGuessBlockDepth(int guessBlockDepth) {
        this.guessBlockDepth = guessBlockDepth;
    }

    public ArrayList<Integer> getWinners() {
        return winners;
    }

    public void setWinners(ArrayList<Integer> winners) {
        this.winners = winners;
    }


}
