package com.ankur.stackoverflow.cache;

import com.ankur.stackoverflow.common.ValueWrapper;
import com.ankur.stackoverflow.utils.LogUtils;

public class FailSafeCache implements Cache {
    private String LOG_TAG = "FailSafeCache";
    private Cache targetCache;
    private static final ValueWrapper NULL_VALUE_WRAPPER = new ValueWrapper(null);

    public FailSafeCache(Cache targetCache) {
        this.targetCache = targetCache;
    }

    @Override
    public void clear() {
        try {
            this.targetCache.clear();
        } catch (Exception eX) {
            //do nothing
        }
    }

    @Override
    public String getName() {
        return this.targetCache.getName();
    }

    @Override
    public Object getNativeCache() {
        return this.targetCache.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        ValueWrapper result = NULL_VALUE_WRAPPER;
        try {
            result = this.targetCache.get(key);

        } catch (Exception eX) {
            String errorString = String.format("Failed to get value for key: %s.", key);
            LogUtils.errorLog(LOG_TAG, errorString, eX);
        }
        return result;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        T result = null;
        try {
            result = this.targetCache.get(key, type);

        } catch (Exception eX) {
            String errorString = String.format("Failed to get value for key: %s and type: %s.", key, type);
            LogUtils.errorLog(LOG_TAG, errorString, eX);
        }
        return result;
    }

    @Override
    public void put(Object key, Object value) {
        try {
            this.targetCache.put(key, value);

        } catch (Exception eX) {
            String errorString = String.format("Failed to put the value %s for key %s.", value, key);
            LogUtils.errorLog(LOG_TAG, errorString, eX);
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        ValueWrapper result = NULL_VALUE_WRAPPER;
        try {
            result = this.targetCache.putIfAbsent(key, value);

        } catch (Exception eX) {
            String errorString = String.format("Failed to putIfAbsent the value %s for key %s.", value, key);
            LogUtils.errorLog(LOG_TAG, errorString, eX);
        }
        return result;
    }

    @Override
    public void evict(Object key) {
        try {
            this.targetCache.evict(key);
        } catch (Exception eX) {
            String errorString = String.format("Failed to evict key: %s.", key);
            LogUtils.errorLog(LOG_TAG, errorString, eX);
        }
    }
}