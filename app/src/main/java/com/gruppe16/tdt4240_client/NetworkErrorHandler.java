package com.gruppe16.tdt4240_client;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by Sigurd on 11.03.2017.
 */

public class NetworkErrorHandler implements Response.ErrorListener {

    @Override
    public void onErrorResponse(VolleyError error) {
        System.out.println(error);
    }
}
