package com.ankur.stackoverflow.cache;

import com.ankur.stackoverflow.common.ValueWrapper;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class ConcurrentMapCache implements Cache {

    private static final Object NULL_HOLDER = new NullHolder();

    private final String name;

    private final ConcurrentMap<Object, Object> store;

    private final boolean allowNullValues;


    /**
     * Create a new ConcurrentMapCache with the specified name.
     *
     * @param name the name of the cache
     */
    public ConcurrentMapCache(String name) {
        this(name, new ConcurrentHashMap<Object, Object>(256), true);
    }

    /**
     * Create a new ConcurrentMapCache with the specified name.
     *
     * @param name            the name of the cache
     * @param allowNullValues whether to accept and convert {@code null}
     *                        values for this cache
     */
    public ConcurrentMapCache(String name, boolean allowNullValues) {
        this(name, new ConcurrentHashMap<Object, Object>(256), allowNullValues);
    }

    /**
     * Create a new ConcurrentMapCache with the specified name and the
     * given internal {@link java.util.concurrent.ConcurrentMap} to use.
     *
     * @param name            the name of the cache
     * @param store           the ConcurrentMap to use as an internal store
     * @param allowNullValues whether to allow {@code null} values
     *                        (adapting them to an internal null holder value)
     */
    public ConcurrentMapCache(String name, ConcurrentMap<Object, Object> store, boolean allowNullValues) {
        this.name = name;
        this.store = store;
        this.allowNullValues = allowNullValues;
    }


    @Override
    public final String getName() {
        return this.name;
    }

    @Override
    public final ConcurrentMap<Object, Object> getNativeCache() {
        return this.store;
    }

    public final boolean isAllowNullValues() {
        return this.allowNullValues;
    }

    @Override
    public ValueWrapper get(Object key) {
        Object value = this.store.get(key);
        return toWrapper(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Class<T> type) {
        Object value = fromStoreValue(this.store.get(key));
        if (value != null && type != null && !type.isInstance(value)) {
            throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
        }
        return (T) value;
    }

    @Override
    public void put(Object key, Object value) {
        this.store.put(key, toStoreValue(value));
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        Object existing = this.store.putIfAbsent(key, value);
        return toWrapper(existing);
    }

    @Override
    public void evict(Object key) {
        this.store.remove(key);
    }

    @Override
    public void clear() {
        this.store.clear();
    }


    /**
     * Convert the given value from the internal store to a user value
     * returned from the get method (adapting {@code null}).
     *
     * @param storeValue the store value
     * @return the value to return to the user
     */
    protected Object fromStoreValue(Object storeValue) {
        if (this.allowNullValues && storeValue == NULL_HOLDER) {
            return null;
        }
        return storeValue;
    }

    /**
     * Convert the given user value, as passed into the put method,
     * to a value in the internal store (adapting {@code null}).
     *
     * @param userValue the given user value
     * @return the value to store
     */
    protected Object toStoreValue(Object userValue) {
        if (this.allowNullValues && userValue == null) {
            return NULL_HOLDER;
        }
        return userValue;
    }

    private ValueWrapper toWrapper(Object value) {
        return (value != null ? new ValueWrapper(fromStoreValue(value)) : null);
    }

    @SuppressWarnings("serial")
    private static class NullHolder implements Serializable {
    }

}