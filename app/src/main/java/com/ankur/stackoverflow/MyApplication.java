package com.ankur.stackoverflow;

import java.util.HashSet;
import java.util.Set;

import android.app.Application;

import com.ankur.stackoverflow.jobs.JobManagerLogger;
import com.ankur.stackoverflow.utils.NetworkConnectivityListener;
import com.ankur.stackoverflow.utils.NetworkUtils;
import com.ankur.stackoverflow.utils.SharedPrefs;
import com.ankur.stackoverflow.utils.VolleyLib;
import com.path.android.jobqueue.JobManager;

import com.path.android.jobqueue.config.Configuration;

public class MyApplication extends Application implements NetworkConnectivityListener.ConnectivityChangedListener {

    private static final String  LOG_TAG                  = "MY_APPLICATION";

    private static MyApplication mMyApplication;

    public static final int      NETWORK_TYPE_WIFI        = 2;

    public static final int      NETWORK_TYPE_CELLULAR    = 1;

    private static Set<String>   sDialogsOnScreen;

    private static boolean       sPlayerExpanded          = false;

    private static int           sNetworkType             = -1;

    private static int           sNetworkSubType          = -1;

    private boolean              mHideNetworkNotification = false;

    private static JobManager    mPersistentJobManager;

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
        sDialogsOnScreen = new HashSet<>();
        VolleyLib.init(this);
        SharedPrefs.getInstance().init(this);
        configureJobManagers();
    }

    /*
     * @Returns true if set is modified. false otherwise.
     */
    public boolean addDialogToSet(String id) {
        return sDialogsOnScreen.add(id);
    }

    public boolean removeDialogFromSet(String id) {
        return sDialogsOnScreen.remove(id);
    }

    private void configureJobManagers() {
        Configuration persistentJobManager = new Configuration.Builder(this).customLogger(new JobManagerLogger())
                .minConsumerCount(1)// always keep at least one consumer alive
                .maxConsumerCount(3)// up to 3 consumers at a time
                .loadFactor(3)// 3 jobs per consumer
                .consumerKeepAlive(120)// wait 2 minute
                .build();
        mPersistentJobManager = new JobManager(this, persistentJobManager);
    }

    public static JobManager getJobManager() {
        return mPersistentJobManager;
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

    public boolean hideNetworkNotification() {
        return mHideNetworkNotification;
    }

    public void setNetworkNotificationVisibility(boolean hideNotification) {
        this.mHideNetworkNotification = hideNotification;
    }

    public void setConfigSyncedExpiry() {
        SharedPrefs.getInstance().setSyncedUserStateExpirationTime(System.currentTimeMillis() + (1000 * 1800));
    }

    public static boolean isConfigSyncedExpired() {
        return System.currentTimeMillis() >= SharedPrefs.getInstance().getSyncedUserStateExpirationTime();
    }
}
