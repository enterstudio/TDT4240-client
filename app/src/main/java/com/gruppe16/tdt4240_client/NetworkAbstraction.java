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

    private static NetworkAbstraction networkAbstraction;

    String url = "http://localhost:8000";
    String gameUrl = url + "/game";
    String userUrl = url + "/user";

    RequestQueue requestQueue;
    Network network;
    Cache cache;

    public NetworkAbstraction(Context context){
        requestQueue = Volley.newRequestQueue(context);
        cache = new DiskBasedCache(context.getCacheDir(), 1024*1024);
        network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
    }

    public synchronized static NetworkAbstraction getInstance(Context context){
        if(networkAbstraction == null){
            networkAbstraction = new NetworkAbstraction(context);
        }

        return networkAbstraction;
    }

    public void joinGame(String gamePin){
        JSONObject payload = new JSONObject();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, payload, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    public void createGame(Response.ResponseListener<JSONObject> listener){

    }

    public void submitDrawing(){

    }

    public void submitGuess(){

    }

    public void getDrawing(){
        // set fragment as listener for receiving drawing
    }

    public void getGuess(){

    }



}
