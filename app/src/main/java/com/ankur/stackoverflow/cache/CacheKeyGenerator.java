package com.ankur.stackoverflow.cache;

import java.util.List;

public class CacheKeyGenerator extends BaseCacheKeyGenerator {
    public static final CacheKeyGenerator instance = new CacheKeyGenerator();
    /**
     * The default cache key separator
     */
    public static final String CACHE_KEY_SEPARATOR = "-";

    public static CacheKeyGenerator getInstance() {
        return instance;
    }

}
