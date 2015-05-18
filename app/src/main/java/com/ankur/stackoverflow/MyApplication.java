package com.ankur.stackoverflow;

import android.app.Application;

import com.ankur.stackoverflow.utils.NetworkConnectivityListener;
import com.ankur.stackoverflow.utils.NetworkUtils;
import com.ankur.stackoverflow.utils.VolleyLib;

public class MyApplication extends Application implements NetworkConnectivityListener.ConnectivityChangedListener {

    private static final String  LOG_TAG                  = "MY_APPLICATION";

    private static MyApplication mMyApplication;

    public static final int      NETWORK_TYPE_WIFI        = 2;

    public static final int      NETWORK_TYPE_CELLULAR    = 1;

    private static int           sNetworkType             = -1;

    private static int           sNetworkSubType          = -1;

    public MyApplication() {
        mMyApplication = this;
    }

    public static MyApplication getMyApplicationContext() {
        return mMyApplication;
    }

    public static int getNetworkType() {
        return sNetworkType;
    }

    public static void setNetworkType(int type) {
        sNetworkType = type;
    }

    public static int getNetworkSubType() {
        return sNetworkSubType;
    }

    public static void setNetworkSubType(int type) {
        sNetworkSubType = type;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        VolleyLib.init(this);
    }

    @Override
    public void onConnectivityChanged(boolean connected, int networkType, int networkSubtype) {
        if (connected) {
            if (NetworkUtils.isConnectedToMI(this)) {
                MyApplication.setNetworkType(MyApplication.NETWORK_TYPE_CELLULAR);
            } else {
                MyApplication.setNetworkType(MyApplication.NETWORK_TYPE_WIFI);
            }
            MyApplication.setNetworkSubType(NetworkUtils.getNetworkType(this));
        }
    }
}
