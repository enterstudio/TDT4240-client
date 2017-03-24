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

import static com.gruppe16.tdt4240_client.MainActivity.round;
import static com.gruppe16.tdt4240_client.fragments.DrawFragment.finishedDrawing;

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


    public void joinGame(String gamePin, Response.Listener<JSONObject> listener){
        String joinGameUrl = gameUrl + "/" + gamePin;
        Request<JSONObject> request = new JsonObjectRequest(POST, joinGameUrl, null, listener, errorListener);
        requestQueue.add(request);
    }


    public void createGame(Response.Listener<JSONObject> listener){
        Request<JSONObject> request = new JsonObjectRequest(POST, gameUrl, null, listener, errorListener);
        requestQueue.add(request);
    }

    public void pollForGame(String gamePin, Response.Listener<JSONObject> listener){
        String url = gameUrl + "/" + gamePin;
        Request<JSONObject> request = new JsonObjectRequest(GET, url, null, listener, errorListener);
        requestQueue.add(request);
    }


    public void submitDrawing(String gamepin, String playerId, Bitmap drawing, Response.Listener<JSONObject> responseHandler){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        drawing.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
        byte [] byte_arr = stream.toByteArray();
        String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("image", image_str);
            jsonParams.put("gamepin", gamepin);
            jsonParams.put("playerId", playerId);
            jsonParams.put("round", round);

            Request<JSONObject> request = new JsonObjectRequest(POST, drawingUrl, jsonParams, responseHandler, errorListener);
            requestQueue.add(request);
            
        }
        catch (Exception e) {

        }
      
    }


    public void submitGuess(String gamepin, String playerId, String guess, Response.Listener<JSONObject> responseHandler){

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("guess", guess);
            jsonParams.put("gamepin", gamepin);
            jsonParams.put("playerId", playerId);
            jsonParams.put("round", round);

            Request<JSONObject> request = new JsonObjectRequest(POST, guessUrl, jsonParams, responseHandler, errorListener);
            requestQueue.add(request);

        }
        catch (Exception e) {

        }
    }

    public Bitmap getPage(String gamepin, String playerId, int round, Response.Listener<JSONObject> responseHandler){
        String url2 = String.format("%s/%s?player=%s&round=%n", gameUrl, gamepin, playerId, round);
        String url = gameUrl + "/" + gamepin + "?player=" + playerId + "&round=" +round;
        Request<JSONObject> request = new JsonObjectRequest(GET, url, null, responseHandler, errorListener);
        requestQueue.add(request);

        //TODO: denne må endres
        return finishedDrawing;
    }

    public void getDrawing(String gamepin, String playerId, Response.Listener<JSONObject> responseHandler){
        /*
        String requestUrl = url + "/drawing/:" + "/0";
        Request<JSONObject> request = new JsonObjectRequest(GET, requestUrl, responseHandler, errorListener);
        requestQueue.add(request);
        */

    }


    public void getGuess(){
        //TODO: implement
    }
}
