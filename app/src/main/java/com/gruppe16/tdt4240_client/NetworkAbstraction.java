package com.gruppe16.tdt4240_client;
import android.app.DownloadManager;
import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by Sigurd on 09.03.2017.
 */

public class NetworkAbstraction {

    private final static int POST = Request.Method.POST;
    private final static int GET = Request.Method.GET;
    private static NetworkAbstraction networkAbstraction;

    private String url = "http://localhost:8000";
    private String gameUrl = url + "/game/";
    private String userUrl = url + "/user";
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


    public void submitGuess(int gamepin, Response.Listener<JSONObject> listener){
        String guessUrl = gameUrl + "/" + gamepin + "/guess";
        Request<JSONObject> request = new JsonObjectRequest(POST, guessUrl, null, listener, errorListener);
        requestQueue.add(request);
    }


    public void submitDrawing(){
        //TODO: implement
    }


    public void getDrawing(){
        //TODO: implement
    }


    public void getGuess(){
        //TODO: implement
    }

}
