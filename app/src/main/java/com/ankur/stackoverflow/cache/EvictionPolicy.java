package com.ankur.stackoverflow.cache;

public enum EvictionPolicy {
    /**
     * volatile-lru -> remove the key with an expire set using an LRU algorithm
     * # allkeys-lru -> remove any key accordingly to the LRU algorithm
     * # volatile-random -> remove a random key with an expire set
     * # allkeys->random -> remove a random key, any key
     * # volatile-ttl -> remove the key with the nearest expire time (minor TTL)
     * # noeviction -> don't expire at all, just return an error on write operations
     */

    ALL_KEYS_LRU("allkeys-lru"),
    VOLATILE_KEYS_LRU("volatile-lru"),
    VOLATILE_KEYS("volatile"),
    ALL_KEYS("allkeys"),
    VOLATILE_KEYS_TTL("volatile-ttl"),
    NO_EVICTION("noeviction");

    private String policy;

    private EvictionPolicy(String policy) {
        this.policy = policy;
    }

    public String policy() {
        return policy;
    }
}
