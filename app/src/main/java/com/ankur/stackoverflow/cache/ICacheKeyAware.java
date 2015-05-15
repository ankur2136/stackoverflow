package com.ankur.stackoverflow.cache;


/**
 * Implemented by classes who intend to be used as a cache key. These classes have to implement
 * <code>getCacheKey()</code> method which returns a {@link String} which can be formed by the combination
 * of fields which are necessary to uniquely identify the object.
 *
 * @see CacheKeyGenerator
 */
public interface ICacheKeyAware {
    String getCacheKey();
}
