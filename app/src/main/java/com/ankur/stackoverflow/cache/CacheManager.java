package com.ankur.stackoverflow.cache;

import java.util.Collection;

public interface CacheManager {
    /**
     * Return the cache associated with the given name.
     *
     * @param name the cache identifier (must not be {@code null})
     * @return the associated cache, or {@code null} if none found
     */
    Cache getCache(String name);

    /**
     * Return a collection of the cache names known by this manager.
     *
     * @return the names of all caches known by the cache manager
     */
    Collection<String> getCacheNames();
}
