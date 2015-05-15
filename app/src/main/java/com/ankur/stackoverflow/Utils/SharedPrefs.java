package com.ankur.stackoverflow.utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

import com.ankur.stackoverflow.common.AsyncUtils;
import com.ankur.stackoverflow.common.PreferenceKeys;

public class SharedPrefs {

    private static final String LOG_TAG = "SHARED_PREFS";

    private Context mContext;

    private SharedPreferences mSharedPreferences;

    private Editor mPreferenceEditor;

    private Map<String, Set<OnSharedPreferenceChangeListener>> mListeners;

    private static SharedPrefs                                 sSharedPrefs;

    public static synchronized SharedPrefs getInstance() {
        if (sSharedPrefs == null) {
            sSharedPrefs = new SharedPrefs();
        }
        return sSharedPrefs;
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mPreferenceEditor = mSharedPreferences.edit();
        mListeners = new ConcurrentHashMap<String, Set<OnSharedPreferenceChangeListener>>();
    }

    public void register(String[] preferenceKeys, OnSharedPreferenceChangeListener listener) {
        for (String preferenceKey : preferenceKeys) {
            register(preferenceKey, listener);
        }
    }

    public void register(String preferenceKey, OnSharedPreferenceChangeListener listener) {
        if (mListeners.containsKey(preferenceKey)) {
            Set<OnSharedPreferenceChangeListener> listeners = mListeners.get(preferenceKey);
            listeners.add(listener);
        } else {
            Set<OnSharedPreferenceChangeListener> listeners = new HashSet<OnSharedPreferenceChangeListener>();
            listeners.add(listener);
            mListeners.put(preferenceKey, listeners);
        }
    }

    public void unregister(String[] preferenceKeys, OnSharedPreferenceChangeListener listener) {
        for (String preferenceKey : preferenceKeys) {
            unregister(preferenceKey, listener);
        }
    }

    public void unregister(String preferenceKey, OnSharedPreferenceChangeListener listener) {
        if (mListeners.containsKey(preferenceKey)) {
            Set<OnSharedPreferenceChangeListener> listeners = mListeners.get(preferenceKey);
            listeners.remove(listener);
            if (listeners.size() == 0) {
                mListeners.remove(preferenceKey);
            }
        } else {
            if (LogUtils.isDebugLogEnabled())
                LogUtils.debugLog(LOG_TAG, "Can't find the listener to unregister");
        }
    }

    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "onSharedPreferenceChanged : " + key);
        AsyncUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mListeners.containsKey(key)) {
                    Set<OnSharedPreferenceChangeListener> listeners = mListeners.get(key);
                    for (OnSharedPreferenceChangeListener listener : listeners) {
                        listener.onSharedPreferenceChanged(sharedPreferences, key);
                    }
                }
            }
        });
    }

    public SharedPreferences.Editor clear() {
        return mPreferenceEditor.clear();
    }

    public boolean commit() {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "Commiting changes...");
        return mPreferenceEditor.commit();
    }

    public void apply() {
        mPreferenceEditor.apply();
    }

    private void updatePreferences(String key, String value) {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "Updating prefrences " + key + " : " + value);
        mPreferenceEditor.putString(key, value);
        apply();
        onSharedPreferenceChanged(mSharedPreferences, key);
    }

    private void updatePreferences(String key, boolean value) {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "Updating prefrences " + key + " : " + value);
        mPreferenceEditor.putBoolean(key, value);
        apply();
        onSharedPreferenceChanged(mSharedPreferences, key);
    }

    private void updatePreferences(String key, long value) {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "Updating prefrences " + key + " : " + value);
        mPreferenceEditor.putLong(key, value);
        apply();
        onSharedPreferenceChanged(mSharedPreferences, key);
    }

    private void updatePreferences(String key, int value) {
        if (LogUtils.isDebugLogEnabled())
            LogUtils.debugLog(LOG_TAG, "Updating prefrences " + key + " : " + value);
        mPreferenceEditor.putInt(key, value);
        apply();
        onSharedPreferenceChanged(mSharedPreferences, key);
    }

    public void setAppLanguage(String langCode) {
        updatePreferences(PreferenceKeys.SELECTED_APP_LANGUAGE_CODE, langCode);
    }

    public String getBaseHostUrl() {
        return mSharedPreferences.getString(PreferenceKeys.BASE_HOST_URL, "http://52.0.75.201");

        // return mSharedPreferences.getString(PreferenceKeys.BASE_HOST_URL,
        // "http://192.168.106.251");
    }

    public void setBaseHostUrl(String url) {
        updatePreferences(PreferenceKeys.BASE_HOST_URL, url);
    }

    public boolean expandPlayer() {
        return mSharedPreferences.getBoolean(PreferenceKeys.EXPAND_PLAYER, true);
    }

    public void setPlayerExpanded() {
        updatePreferences(PreferenceKeys.EXPAND_PLAYER, false);
    }

    public boolean isScanComplete() {
        return mSharedPreferences.getBoolean(PreferenceKeys.IS_SCAN_COMPLETE, false);
    }

    public void setScanComplete() {
        updatePreferences(PreferenceKeys.IS_SCAN_COMPLETE, true);
    }

    public void setDeviceId(String deviceId) {
        updatePreferences(PreferenceKeys.DEVICE_ID, deviceId);
    }

    public String getDeviceId() {
        return mSharedPreferences.getString(PreferenceKeys.DEVICE_ID, "");
    }

    public String getUserToken() {
        return mSharedPreferences.getString(PreferenceKeys.USER_TOKEN, null);
    }

    public void setUserToken(String token) {
        updatePreferences(PreferenceKeys.USER_TOKEN, token);
    }

    public String getUserId() {
        return mSharedPreferences.getString(PreferenceKeys.USER_ID, null);
    }

    public void setUserId(String userId) {
        updatePreferences(PreferenceKeys.USER_ID, userId);
    }

    public long getSyncedUserStateExpirationTime() {
        return mSharedPreferences.getLong(PreferenceKeys.SYNCED_USER_STATE_EXPIRATION_TIME, -1);
    }

    public void setSyncedUserStateExpirationTime(long timestamp) {
        updatePreferences(PreferenceKeys.SYNCED_USER_STATE_EXPIRATION_TIME, timestamp);
    }

    public boolean getIsInternetOn() {
        return mSharedPreferences.getBoolean(PreferenceKeys.ALLOW_INTERNET_ACCESS, true);
    }

    public void setUseInternet(boolean value) {
        updatePreferences(PreferenceKeys.ALLOW_INTERNET_ACCESS, value);
    }

    public boolean getUseWifiOnly() {
        return mSharedPreferences.getBoolean(PreferenceKeys.USE_WIFI_ONLY, false);
    }

    public void setUseWifiOnly(boolean value) {
        updatePreferences(PreferenceKeys.USE_WIFI_ONLY, value);
    }

    public void setMaxItemsCount(int count) {
        updatePreferences(PreferenceKeys.MAX_ITEMS, count);
    }

    public int getMaxItemsCount() {
        return mSharedPreferences.getInt(PreferenceKeys.MAX_ITEMS, 10000);
    }

    public void setExpiryTaskLastRunTimestamp(long timestamp) {
        updatePreferences(PreferenceKeys.EXPIRY_TASK_RUNNING, timestamp);
    }

    public long getExpiryTaskLastRunTimestamp() {
        return mSharedPreferences.getLong(PreferenceKeys.EXPIRY_TASK_RUNNING, 0);
    }

    public String getGcmRegistrationId() {
        return mSharedPreferences.getString(PreferenceKeys.GCM_REGISTRATION_ID, null);
    }

    public void setGcmRegistrationId(String id) {
        updatePreferences(PreferenceKeys.GCM_REGISTRATION_ID, id);
    }

    public long getGcmExpirationTimestamp() {
        return mSharedPreferences.getLong(PreferenceKeys.GCM_EXPIRATION_TIME, -1);
    }

    public void setGcmExpirationTimestamp(long timestamp) {
        updatePreferences(PreferenceKeys.GCM_EXPIRATION_TIME, timestamp);
    }

    public int getGcmOSVersion() {
        return mSharedPreferences.getInt(PreferenceKeys.GCM_OS_VERSION, Integer.MIN_VALUE);
    }

    public void setGcmOSVersion(int version) {
        updatePreferences(PreferenceKeys.GCM_OS_VERSION, version);
    }

    public int getGcmAppVersion() {
        return mSharedPreferences.getInt(PreferenceKeys.GCM_APP_VERSION, Integer.MIN_VALUE);
    }

    public void setGcmAppVersion(int version) {
        updatePreferences(PreferenceKeys.GCM_APP_VERSION, version);
    }

    public long getGcmRetryBackoffTime() {
        return mSharedPreferences.getLong(PreferenceKeys.GCM_RETRY_BACKOFF_TIME, 1000);
    }

    public void setGcmRetryBackoffTime(long backOffTimeMs) {
        updatePreferences(PreferenceKeys.GCM_RETRY_BACKOFF_TIME, backOffTimeMs);
    }

    public boolean isGcmIdSyncedWithServer() {
        return mSharedPreferences.getBoolean(PreferenceKeys.GCM_ID_SYNCED, false);
    }

    public void setGcmIdSyncedWithServer(boolean isSynced) {
        updatePreferences(PreferenceKeys.GCM_ID_SYNCED, isSynced);
    }

    public void setGooglePlayServicesEnabled(boolean gpsEnabled) {
        updatePreferences(PreferenceKeys.GPS_ENABLED, gpsEnabled);
        commit();
    }

    public boolean isGooglePlayServicesEnabled() {
        return mSharedPreferences.getBoolean(PreferenceKeys.GPS_ENABLED, false);
    }
}