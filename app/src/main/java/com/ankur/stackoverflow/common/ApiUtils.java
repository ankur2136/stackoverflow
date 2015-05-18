package com.ankur.stackoverflow.common;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
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
}