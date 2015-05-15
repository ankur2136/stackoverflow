package com.ankur.stackoverflow.cache;

public class KeyInfo {
    private long expireTime;

    public KeyInfo(long expireTime) {
        this.expireTime = expireTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}
