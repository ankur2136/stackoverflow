package com.ankur.stackoverflow.cache;

import com.ankur.stackoverflow.common.ValueWrapper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocalConcurrentMapCache extends ConcurrentMapCache implements IEvictionPolicy {
    private final long expiration;
    private final ConcurrentMap<Object, KeyInfo> keysInfoMap = new ConcurrentHashMap<>(256);
    protected long maxSize;

    public static LocalConcurrentMapCache createCache(String name, ConcurrentMap<Object, Object> store,
                                                      long expiration, long maxSize, EvictionPolicy evictionPolicy) {
        if (evictionPolicy == EvictionPolicy.ALL_KEYS_LRU) {
            return new LRUCache(name, new ConcurrentHashMap<>(256), expiration, maxSize);
        }
        return new LocalConcurrentMapCache(name, new ConcurrentHashMap<>(256), expiration, maxSize);
    }

    public LocalConcurrentMapCache(String name, ConcurrentMap<Object, Object> store, long expiration, long maxSize) {
        super(name, store, true);
        this.expiration = expiration;
        this.maxSize = maxSize;
    }

    public ValueWrapper get(Object key) {
        if (expiration > 0) {
            KeyInfo keyInfo = keysInfoMap.get(key);
            if (keyInfo == null) {
                return null;
            }

            if (System.currentTimeMillis() > keyInfo.getExpireTime()) {
                evict(key);
                return null;
            }
        }
        return super.get(key);
    }

    public void put(Object key, Object value) {
        super.put(key, value);
        if (expiration > 0) {
            keysInfoMap.put(key, new KeyInfo(System.currentTimeMillis() + (expiration * 1000)));
        }
    }

    public void evict(Object key) {
        super.evict(key);
        keysInfoMap.remove(key);
    }

    public ConcurrentMap<Object, Object> getStore() {
        return getNativeCache();
    }

    public ConcurrentMap<Object, KeyInfo> getKeysInfoMap() {
        return keysInfoMap;
    }

    @Override
    public boolean evictionRequired() {
        return maxSize > 0 && getNativeCache().size() >= maxSize;
    }

    @Override
    public void evict() {

    }
}