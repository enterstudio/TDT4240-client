package com.gruppe16.tdt4240_client;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Map;


/**
 * Created by Sigurd on 09.03.2017.
 */

public class NetworkAbstraction {

    private final static int POST = Request.Method.POST;
    private final static int GET = Request.Method.GET;
    private static NetworkAbstraction networkAbstraction;

    private String url = "http://10.0.2.2:8000";
    private String gameUrl = url + "/game";
    private String userUrl = url + "/user";
    private String guessUrl = url + "/guess";
    private String drawingUrl = url + "/drawing";
    private String scoreUrl = url + "/score";
    private String startGameUrl = url + "/game/";
    private RequestQueue requestQueue;
    private NetworkErrorHandler errorListener = new NetworkErrorHandler();


    private NetworkAbstraction(Context context){
        requestQueue = Volley.newRequestQueue(context);
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024*1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
    }


    public synchronized static NetworkAbstraction getInstance(Context context){
        if(networkAbstraction == null){
            networkAbstraction = new NetworkAbstraction(context);
        }
        return networkAbstraction;
    }

    public void startGame(Response.Listener<JSONObject> listener){
        String startGameUrl = gameUrl + "/" + GameState.getInstance().getGamePin() + "/start";
        Request<JSONObject> request = new JsonObjectRequest(POST, startGameUrl, null, listener, errorListener);
        requestQueue.add(request);
    }

    public void joinGame(Response.Listener<JSONObject> listener){
        String joinGameUrl = gameUrl + "/" + GameState.getInstance().getGamePin() + "/join";
        Request<JSONObject> request = new JsonObjectRequest(POST, joinGameUrl, null, listener, errorListener);
        requestQueue.add(request);
    }


    public void createGame(Response.Listener<JSONObject> listener){
        Request<JSONObject> request = new JsonObjectRequest(POST, gameUrl, null, listener, errorListener);
        requestQueue.add(request);
    }

    public void pollForGame(Response.Listener<JSONObject> listener){
        String url = gameUrl + "/" + GameState.getInstance().getGamePin();
        Request<JSONObject> request = new JsonObjectRequest(GET, url, null, listener, errorListener);
        requestQueue.add(request);
    }


    public void submitDrawing(Response.Listener<JSONObject> responseHandler){
        GameState gameState = GameState.getInstance();
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("image", gameState.getImageString());
            jsonParams.put("gamePin", gameState.getGamePin());
            jsonParams.put("playerId", gameState.getMyPlayerId());
            jsonParams.put("round", gameState.getRound());

            Request<JSONObject> request = new JsonObjectRequest(POST, drawingUrl, jsonParams, responseHandler, errorListener);
            requestQueue.add(request);
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void submitGuess(Response.Listener<JSONObject> responseHandler){
        /*
        GameState gameState = GameState.getInstance();
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("guess", guess);
            jsonParams.put("gamePin", gameState.getGamePin());
            jsonParams.put("playerId", gameState.getMyPlayerId());
            jsonParams.put("round", gameState.getRound());

            Request<JSONObject> request = new JsonObjectRequest(POST, guessUrl, jsonParams, responseHandler, errorListener);
            requestQueue.add(request);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        */
    }

    public void getPage(Response.Listener<JSONObject> responseHandler){
        GameState gameState = GameState.getInstance();
        String url = gameUrl + "/" + gameState.getGamePin() + "?player=" + gameState.getMyPlayerId() + "&round=" + gameState.getRound();
        Request<JSONObject> request = new JsonObjectRequest(GET, url, null, responseHandler, errorListener);
        requestQueue.add(request);

        //String url2 = String.format("%s/%s?player=%s&round=%n", gameUrl, gamePin, myPlayerId, roundCounter);
    }

    public void getDrawing(Response.Listener<JSONObject> responseHandler){
        String requestUrl = drawingUrl + "/" + GameState.getInstance().getDrawingId();
        Request<JSONObject> request = new JsonObjectRequest(GET, requestUrl, null, responseHandler, errorListener);
        requestQueue.add(request);
    }

    public void submitScore(Map<String, Integer> map, Response.Listener<JSONObject> listener){
        JSONObject jsonParams = new JSONObject();
        try {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String key = entry.getKey();
                int value = entry.getValue();
                jsonParams.put(key, value);
            }
            Request<JSONObject> request = new JsonObjectRequest(POST, scoreUrl, jsonParams, listener, errorListener);
            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getGuess(){
        //TODO: implement
    }
}
