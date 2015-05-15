package com.ankur.stackoverflow.cache;

public interface IEvictionPolicy {
    
    boolean evictionRequired();

    void evict();
}
