package com.ankur.stackoverflow.common;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.RequestFuture;
import com.ankur.stackoverflow.MyApplication;
import com.ankur.stackoverflow.utils.JsonObjectSignedRequest;
import com.ankur.stackoverflow.utils.LogUtils;
import com.ankur.stackoverflow.utils.NetworkUtils;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RequestUtils {
    private static final String LOG_TAG      = "REQUEST_UTILS";

    public static final int     LOW_TIMEOUT  = 10000;

    public static final int     HIGH_TIMEOUT = 20000;

    public static final int     MAX_RETRIES  = 1;

    public static final float   BACKOFF_MULT = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;

    public static <T> JsonRequest<T> getRequest(T object, Context context, int method, String url,
            JSONObject jsonRequest, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        JsonRequest<T> request = new JsonObjectSignedRequest(object, method, url, jsonRequest, listener, errorListener);
        setRequestRetryPolicy(context, request);
        return request;
    }

    public static Object getFuture(RequestFuture future, Context context, int timeOutMillis) throws Exception {
        try {
            if (timeOutMillis == 0) {
                timeOutMillis = RequestUtils.getTimeout(context);
            }

            return future.get(timeOutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            LogUtils.warnLog(LOG_TAG, "Request interrupted", ex);
            throw ex;
        } catch (TimeoutException ex) {
            LogUtils.warnLog(LOG_TAG, "Request timeout ", ex);
            throw ex;
        } catch (Exception ex) {
            LogUtils.warnLog(LOG_TAG, "Failed to get response", ex);
            throw ex;
        }
    }

    public static int getTimeout(Context context) {
        if (NetworkUtils.isConnectedFast(context)) {
            return LOW_TIMEOUT;
        } else {
            return HIGH_TIMEOUT;
        }
    }

    private static void setRequestRetryPolicy(Context context, Request<?> request) {
        request.setRetryPolicy(new DefaultRetryPolicy(getTimeout(context), MAX_RETRIES, BACKOFF_MULT));
    }
}
