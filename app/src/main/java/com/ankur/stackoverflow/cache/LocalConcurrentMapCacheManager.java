package com.ankur.stackoverflow.cache;

import com.ankur.stackoverflow.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class LocalConcurrentMapCacheManager extends ConcurrentMapCacheManager {
    private String LOG_TAG = "LocalConcurrentMapCacheManager";
    private Map<String, Long> expires = null;
    private Map<String, Long> maxSizes = null;
    private Map<String, EvictionPolicy> evictionPolicyMap = null;

    List<LocalConcurrentMapCache> caches = new ArrayList<>();

    public LocalConcurrentMapCacheManager() {
    }

    /**
     * Start timer thread which will expire cache keys.
     */
    public void startExpiryTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    expireKeys();
                } catch (Exception e) {
                    LogUtils.errorLog(LOG_TAG, String.format("Cannot expire keys for cache manager [%s]", getClass().getName()), e);
                }
            }
        }, 10 * 60 * 1000, 10 * 60 * 1000);
    }

    /**
     * Sets the expire time (in seconds) for cache regions
     *
     * @param expires time in seconds
     */
    public void setExpires(Map<String, Long> expires) {
        this.expires = (expires != null ? new ConcurrentHashMap<>(expires) : null);
    }

    /**
     * Sets the max sizes for cache regions
     *
     * @param maxSizes
     */
    public void setMaxSizes(Map<String, Long> maxSizes) {
        this.maxSizes = maxSizes;
    }

    /**
     * sets the eviction policies for cache regions
     *
     * @param evictionPolicyMap
     */
    public void setEvictionPolicyMap(Map<String, EvictionPolicy> evictionPolicyMap) {
        this.evictionPolicyMap = evictionPolicyMap;
    }

    protected Cache createConcurrentMapCache(String name) {
        long expiration = computeExpiration(name);
        long maxSize = computeMaxSize(name);
        EvictionPolicy evictionPolicy = computeEvictionPolicy(name);
        LocalConcurrentMapCache localConcurrentMapCache = getLocalConcurrentCache(name, new ConcurrentHashMap<>(256),
                expiration, maxSize, evictionPolicy);
        caches.add(localConcurrentMapCache);
        return localConcurrentMapCache;
    }

    private long computeExpiration(String name) {
        Long expiration = null;
        if (expires != null) {
            expiration = expires.get(name);
        }
        return (expiration != null ? expiration : -1);
    }

    private long computeMaxSize(String name) {
        Long maxSize = null;
        if (maxSizes != null) {
            maxSize = maxSizes.get(name);
        }
        return (maxSize != null ? maxSize : -1);
    }

    private EvictionPolicy computeEvictionPolicy(String name) {
        EvictionPolicy evictionPolicy = null;
        if (evictionPolicyMap != null) {
            evictionPolicy = evictionPolicyMap.get(name);
        }
        return (evictionPolicy != null ? evictionPolicy : EvictionPolicy.NO_EVICTION);
    }

    private void expireKeys() {
        long currentTime = System.currentTimeMillis();
        for (LocalConcurrentMapCache localConcurrentMapCache : caches) {
            ConcurrentMap<Object, KeyInfo> keysInfoMap = localConcurrentMapCache.getKeysInfoMap();

            for (Map.Entry<Object, KeyInfo> entry : keysInfoMap.entrySet()) {
                KeyInfo keyInfo = entry.getValue();
                if (keyInfo != null && System.currentTimeMillis() > keyInfo.getExpireTime()) {
                    localConcurrentMapCache.evict(entry.getKey());
                }
            }
        }
        LogUtils.infoLog(LOG_TAG, String.format("[Full scan] Time taken to expire keys in local store [%s ms]", (System.currentTimeMillis() - currentTime)));
    }

    private LocalConcurrentMapCache getLocalConcurrentCache(String name, ConcurrentMap<Object, Object> store, long expiration, long maxSize, EvictionPolicy evictionPolicy) {
        if (evictionPolicy == EvictionPolicy.ALL_KEYS_LRU) {
            return new LRUCache(name, new ConcurrentHashMap<>(256), expiration, maxSize);
        }
        return new LocalConcurrentMapCache(name, new ConcurrentHashMap<>(256), expiration, maxSize);
    }
}
