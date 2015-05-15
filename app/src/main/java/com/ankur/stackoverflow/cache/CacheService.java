package com.ankur.stackoverflow.cache;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CacheService {
    private static final CacheManager cacheManager;

    static {
        LocalConcurrentMapCacheManager localConcurrentMapCacheManager = new LocalConcurrentMapCacheManager();
        localConcurrentMapCacheManager.setExpires(getExpireMap());
        localConcurrentMapCacheManager.setMaxSizes(getMaxSizeMap());
        localConcurrentMapCacheManager.setEvictionPolicyMap(getEvictionPolicyMap());

        localConcurrentMapCacheManager.startExpiryTimer();
        cacheManager = new FailSafeCacheManager(localConcurrentMapCacheManager);
    }

    private static Map<String, Long> getExpireMap() {
        Map<String, Long> expireMap = new HashMap<String, Long>();
        Field[] fields = CacheConstants.class.getFields();
        for (Field field : fields) {
            CacheConfig cacheConfig = field.getAnnotation(CacheConfig.class);
            if (cacheConfig != null) {
                try {
                    expireMap.put((String) field.get(null), cacheConfig.ttl());
                } catch (IllegalAccessException iae) {
                    // ignore
                }
            }
        }
        return expireMap;
    }

    private static Map<String, Long> getMaxSizeMap() {
        Map<String, Long> maxSizeMap = new HashMap<String, Long>();
        Field[] fields = CacheConstants.class.getFields();
        for (Field field : fields) {
            CacheConfig cacheConfig = field.getAnnotation(CacheConfig.class);
            if (cacheConfig != null) {
                try {
                    maxSizeMap.put((String) field.get(null), cacheConfig.maxSize());
                } catch (IllegalAccessException iae) {
                    // ignore
                }
            }
        }
        return maxSizeMap;
    }

    private static Map<String, EvictionPolicy> getEvictionPolicyMap() {
        Map<String, EvictionPolicy> evictionPolicyMap = new HashMap<String, EvictionPolicy>();
        Field[] fields = CacheConstants.class.getFields();
        for (Field field : fields) {
            CacheConfig cacheConfig = field.getAnnotation(CacheConfig.class);
            if (cacheConfig != null) {
                try {
                    if (cacheConfig.ttl() != -1) {
                        evictionPolicyMap.put((String) field.get(null), cacheConfig.evictionPolicy());
                    }
                } catch (IllegalAccessException iae) {
                    // ignore
                }
            }
        }
        return evictionPolicyMap;
    }

    public static Cache getCache(String cacheName) {
        return cacheManager.getCache(cacheName);
    }

    public static Object get(String cacheName, Object key) {
        return getCache(cacheName).get(key);
    }

    public static void put(String cacheName, Object key, Object value) {
        getCache(cacheName).put(key, value);
    }

    public static void evict(String cacheName, Object key) {
        getCache(cacheName).evict(key);
    }
}
