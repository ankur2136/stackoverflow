package com.ankur.stackoverflow.utils;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.ankur.stackoverflow.MyApplication;

import org.json.JSONObject;

public class JsonObjectSignedRequest<T> extends JsonRequest<T> {
    private static final String LOG_TAG = "JSON_OBJECT_SIGNED_REQUEST";

    private final int mNetworkType;

    private final int mNetworkSubType;

    private final Object mObject;

    private final Context mContext;

    private OnJsonObjectReceivedListener mOnJsonObjectReceivedListener;

    public JsonObjectSignedRequest(Object object, int method, String url, JSONObject jsonRequest, Listener<T> listener,
                                   ErrorListener errorListener, MyApplication application) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener, errorListener);
        this.mObject = object;
        this.mNetworkType = MyApplication.getNetworkType();
        this.mNetworkSubType = MyApplication.getNetworkSubType();
        this.mContext = application;
    }

    @Override
    public com.android.volley.Request.Priority getPriority() {
        return Priority.HIGH;
    }

    public void setOnJsonObjectReceivedListener(OnJsonObjectReceivedListener listener) {
        mOnJsonObjectReceivedListener = listener;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            if (LogUtils.isDebugLogEnabled())
                LogUtils.debugLog(LOG_TAG, jsonString);
            JSONObject jsonObject = new JSONObject(jsonString);
            if (mOnJsonObjectReceivedListener != null) {
                mOnJsonObjectReceivedListener.onJsonObjectReceived(jsonObject);
            }
            T object = Utils.fromJsonObject(jsonObject, mObject);
            return Response.success(object, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    public interface OnJsonObjectReceivedListener {
        public void onJsonObjectReceived(JSONObject jsonObject);
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
    }
}