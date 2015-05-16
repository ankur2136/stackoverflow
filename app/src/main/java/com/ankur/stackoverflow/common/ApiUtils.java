package com.ankur.stackoverflow.common;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.RequestFuture;
import com.ankur.stackoverflow.domain.ParsingObject;
import com.ankur.stackoverflow.utils.ApiResponseListener;
import com.ankur.stackoverflow.utils.LogUtils;
import com.ankur.stackoverflow.utils.VolleyLib;

public class ApiUtils extends RequestUtils {

    private static final String LOG_TAG = "API_UTILS";

    /**
     * Sync Http call. Waits on current thread. DO NOT USE SYNC CALLS FROM UI THREAD!
     *
     * @param t
     * @param context
     * @param url
     * @param timeout
     * @param listener
     * @param <T>
     */
    public static <T extends ParsingObject> void makeSyncGetJsonRequest(T t, final Context context, String url,
            int timeout, ApiResponseListener<T> listener) {
        RequestFuture<T> future = RequestFuture.newFuture();
        JsonRequest<?> request = getRequest(t, context, Request.Method.GET, url, null, future, future);
        request.setRetryPolicy(new DefaultRetryPolicy(getTimeout(context), 0, 0));
        VolleyLib.getRequestQueue().add(request);
        try {
            t = (T) getFuture(future, context, timeout);
            listener.onResponse(t);
        } catch (Exception ex) {
            LogUtils.errorLog(LOG_TAG, "Failed to make sync get json request", ex);
            listener.onError(ex);
        }
    }

    public static <JSONObject> void makeSyncGetJsonRequest(final JSONObject json, final Context context, String url,
            int timeout, ApiResponseListener<JSONObject> listener) {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonRequest<?> request = getRequest(json, context, Request.Method.GET, url, null, future, future);
        request.setRetryPolicy(new DefaultRetryPolicy(getTimeout(context), 0, 0));
        VolleyLib.getRequestQueue().add(request);
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) getFuture(future, context, timeout);
            listener.onResponse(jsonObject);
        } catch (Exception ex) {
            LogUtils.errorLog(LOG_TAG, "Failed to make sync get json request", ex);
            listener.onError(ex);
        }
    }

    public static <T extends ParsingObject> void makeAsyncGetJsonRequest(T t, final Context context, String url,
            final ApiResponseListener<T> listener) {
        JsonRequest<?> request = getRequest(t, context, Request.Method.GET, url, null, listener, listener);
        request.setRetryPolicy(new DefaultRetryPolicy(getTimeout(context), 0, 0));
        VolleyLib.getRequestQueue().add(request);
    }

    public static <T extends ParsingObject> void makeSyncPostJsonRequest(T t, final Context context,
            JSONObject payload, String url, int timeout, ApiResponseListener<T> listener) {
        RequestFuture<T> future = RequestFuture.newFuture();
        JsonRequest<?> request = getRequest(t, context, Request.Method.POST, url, payload, future, future);
        request.setRetryPolicy(new DefaultRetryPolicy(getTimeout(context), 0, 0));
        VolleyLib.getRequestQueue().add(request);
        try {
            t = (T) getFuture(future, context, timeout);
            listener.onResponse(t);
        } catch (Exception ex) {
            LogUtils.errorLog(LOG_TAG, "Failed to make sync post json request", ex);
            listener.onError(ex);
        }
    }

    public static <T extends ParsingObject> void makeAsyncPostJsonRequest(T t, final Context context,
            JSONObject payload, String url, final ApiResponseListener<T> listener) {
        JsonRequest<?> request = getRequest(t, context, Request.Method.POST, url, payload, listener, listener);
        request.setRetryPolicy(new DefaultRetryPolicy(getTimeout(context), 0, 0));
        VolleyLib.getRequestQueue().add(request);
    }

    public static Bitmap downloadImage(String url) {
        RequestFuture<Bitmap> future = RequestFuture.newFuture();
        Request<Bitmap> request = new ImageRequest(url, future, 0, 0, Bitmap.Config.RGB_565, future);
        request.setShouldCache(true);
        VolleyLib.getRequestQueue().add(request);
        Bitmap bmp = null;
        try {
            bmp = future.get();
        } catch (Exception ex) {
            LogUtils.errorLog(LOG_TAG, "Failed to download bitmap", ex);
        }
        return bmp;
    }



}