package com.ankur.stackoverflow.cache;

import com.ankur.stackoverflow.common.ValueWrapper;

/**
 * Interface that defines the common cache operations.
 * <p/>
 * <b>Note:</b> Due to the generic use of caching, it is recommended that
 * implementations allow storage of <tt>null</tt> values (for example to
 * cache methods that return {@code null}).
 */
public interface Cache {
    /**
     * Return the cache name.
     */
    String getName();

    /**
     * Return the the underlying native cache provider.
     */
    Object getNativeCache();

    /**
     * Return the value to which this cache maps the specified key.
     * <p>Returns {@code null} if the cache contains no mapping for this key;
     * otherwise, the cached value (which may be {@code null} itself) will
     * be returned in a {@link ValueWrapper}.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which this cache maps the specified key,
     * contained within a {@link ValueWrapper} which may also hold
     * a cached {@code null} value. A straight {@code null} being
     * returned means that the cache contains no mapping for this key.
     * @see #get(Object, Class)
     */
    ValueWrapper get(Object key);

    /**
     * Return the value to which this cache maps the specified key,
     * generically specifying a type that return value will be cast to.
     * <p>Note: This variant of {@code get} does not allow for differentiating
     * between a cached {@code null} value and no cache entry found at all.
     * Use the standard {@link #get(Object)} variant for that purpose instead.
     *
     * @param key  the key whose associated value is to be returned
     * @param type the required type of the returned value (may be
     *             {@code null} to bypass a type check; in case of a {@code null}
     *             value found in the cache, the specified type is irrelevant)
     * @return the value to which this cache maps the specified key
     * (which may be {@code null} itself), or also {@code null} if
     * the cache contains no mapping for this key
     * @throws IllegalStateException if a cache entry has been found
     *                               but failed to match the specified type
     * @see #get(Object)
     */
    <T> T get(Object key, Class<T> type);

    /**
     * Associate the specified value with the specified key in this cache.
     * <p>If the cache previously contained a mapping for this key, the old
     * value is replaced by the specified value.
     *
     * @param key   the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     */
    void put(Object key, Object value);

    /**
     * Atomically associate the specified value with the specified key in this cache
     * if it is not set already.
     * <p>This is equivalent to:
     * <pre><code>
     * Object existingValue = cache.get(key);
     * if (existingValue == null) {
     *     cache.put(key, value);
     *     return null;
     * } else {
     *     return existingValue;
     * }
     * </code></pre>
     * except that the action is performed atomically.
     *
     * @param key   the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     * @return the value to which this cache maps the specified key (which may be
     * {@code null} itself), or also {@code null} if the cache did not contain any
     * mapping for that key prior to this call. Returning {@code null} is therefore
     * an indicator that the given {@code value} has been associated with the key.
     */
    ValueWrapper putIfAbsent(Object key, Object value);

    /**
     * Evict the mapping for this key from this cache if it is present.
     *
     * @param key the key whose mapping is to be removed from the cache
     */
    void evict(Object key);

    /**
     * Remove all mappings from the cache.
     */
    void clear();
}
