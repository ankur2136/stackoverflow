package com.ankur.stackoverflow.utils;

import com.android.volley.Response;

public abstract class ApiResponseListener<T> implements Response.Listener<T>, Response.ErrorListener {

    public void onErrorResponse(com.android.volley.VolleyError volleyError) {
        onError(volleyError);
    }

    public abstract void onError(Exception exception);

}
