package com.ankur.stackoverflow.cache;

public class CacheConstants {

    @CacheConfig(ttl = -1)
    public static final String CACHE_NEVER_EXPIRE = "never-expire";

    @CacheConfig(ttl = 5)
    public static final String CACHE_FIVE_SEC = "5-sec";

    @CacheConfig(ttl = 20)
    public static final String CACHE_TWENTY_SEC = "20-sec";

    @CacheConfig(ttl = 1 * 60)
    public static final String CACHE_ONE_MIN = "1-min";

    @CacheConfig(ttl = 2 * 60)
    public static final String CACHE_TWO_MIN = "2-min";

    @CacheConfig(ttl = 5 * 60)
    public static final String CACHE_FIVE_MIN = "5-min";

    @CacheConfig(ttl = 10 * 60)
    public static final String CACHE_TEN_MIN = "10-min";

    @CacheConfig(ttl = 15 * 60)
    public static final String CACHE_FIFTEEN_MIN = "15-min";

    @CacheConfig(ttl = 30 * 60)
    public static final String CACHE_THIRTY_MIN = "30-min";

    @CacheConfig(ttl = 60 * 60)
    public static final String CACHE_ONE_HOUR = "1-hour";

    @CacheConfig(ttl = 3 * 60 * 60)
    public static final String CACHE_THREE_HOUR = "3-hour";

    @CacheConfig(ttl = 6 * 60 * 60)
    public static final String CACHE_SIX_HOUR = "6-hour";

    @CacheConfig(ttl = 24 * 60 * 60)
    public static final String CACHE_ONE_DAY = "1-day";

    @CacheConfig(ttl = 10 * 24 * 60 * 60)
    public static final String CACHE_10_DAY = "10-day";

    @CacheConfig(ttl = 30 * 60, maxSize = 2000, evictionPolicy = EvictionPolicy.ALL_KEYS_LRU)
    public static final String CACHE_THIRTY_MIN_LRU = "30-min_lru";

    @CacheConfig(ttl = 5 * 60, maxSize = 2000, evictionPolicy = EvictionPolicy.ALL_KEYS_LRU)
    public static final String CACHE_5_MIN_LRU = "5-min_lru";
}
