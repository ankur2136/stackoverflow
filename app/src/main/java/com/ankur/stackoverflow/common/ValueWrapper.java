package com.ankur.stackoverflow.common;

public class ValueWrapper<T> {

    private T value;

    private boolean cacheHit;

    public ValueWrapper() {

    }

    public ValueWrapper(T value) {
        this.value = value;
    }

    public ValueWrapper(T value, boolean cacheHit) {
        this.value = value;
        this.cacheHit = cacheHit;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean isCacheHit() {
        return cacheHit;
    }

    public void setCacheHit(boolean cacheHit) {
        this.cacheHit = cacheHit;
    }
}
