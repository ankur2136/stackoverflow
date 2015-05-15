package com.ankur.stackoverflow.utils;

import java.util.HashSet;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ankur.stackoverflow.MyApplication;

public class NetworkConnectivityListener {

    private static final String LOG_TAG = "NETWORK_CONNECTIVITY_LISTENER";

    private Context mContext;

    private boolean mListening;

    private boolean mConnected;

    private int mNetworkType;

    private int mNetworkSubtype;

    private NetworkInfo mNetworkInfo;

    private Set<ConnectivityChangedListener> mListeners = new HashSet<>();

    private ConnectivityBroadcastReceiver mReceiver;

    private static final NetworkConnectivityListener INSTANCE = new NetworkConnectivityListener();

    public static NetworkConnectivityListener getInstance() {
        return INSTANCE;
    }

    private class ConnectivityBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (!action.equals(ConnectivityManager.CONNECTIVITY_ACTION) || !mListening) {
                return;
            }

            boolean connected = NetworkUtils.isConnected(context);
            NetworkInfo networkInfo = NetworkUtils.getActiveNetworkInfo(context);
            int networkType = -1;
            int networkSubtype = -1;
            if (networkInfo != null) {
                networkType = networkInfo.getType();
                networkSubtype = networkInfo.getSubtype();
            }

            if (mConnected == connected && mNetworkType == networkType && mNetworkSubtype == networkSubtype) {
                LogUtils.debugLog(LOG_TAG, "No network change detected");
                return;
            }

            mConnected = connected;
            mNetworkInfo = networkInfo;
            mNetworkType = networkType;
            mNetworkSubtype = networkSubtype;

            LogUtils.debugLog(LOG_TAG, "[" + mConnected + ", " + mNetworkType + ", " + mNetworkSubtype + "]");

            for (ConnectivityChangedListener listener : mListeners) {
                listener.onConnectivityChanged(mConnected, mNetworkType, mNetworkSubtype);
            }
        }
    }

    private NetworkConnectivityListener() {
        mContext = MyApplication.getMyApplicationContext();
        mConnected = NetworkUtils.isConnected(MyApplication.getMyApplicationContext());
        mReceiver = new ConnectivityBroadcastReceiver();
    }

    public void addListener(ConnectivityChangedListener listener) {
        mListeners.add(listener);
        if (mListeners.size() > 0) {
            startListening();
        }
    }

    public void removeListener(ConnectivityChangedListener listener) {
        mListeners.remove(listener);
        if (mListeners.size() == 0) {
            stopListening();
        }
    }

    private synchronized void startListening() {
        if (!mListening) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            mContext.registerReceiver(mReceiver, filter);
            mConnected = NetworkUtils.isConnected(mContext);
            mNetworkInfo = NetworkUtils.getActiveNetworkInfo(mContext);
            mNetworkType = mNetworkInfo == null ? -1 : mNetworkInfo.getType();
            mNetworkSubtype = mNetworkInfo == null ? -1 : mNetworkInfo.getSubtype();
            mListening = true;
        }
    }

    private synchronized void stopListening() {
        if (mListening) {
            mContext.unregisterReceiver(mReceiver);
            mConnected = false;
            mNetworkType = -1;
            mNetworkSubtype = -1;
            mNetworkInfo = null;
            mListening = false;
        }
    }

    public interface ConnectivityChangedListener {
        public void onConnectivityChanged(boolean connected, int networkType, int networkSubtype);
    }
}