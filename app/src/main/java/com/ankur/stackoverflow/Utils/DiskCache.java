package com.ankur.stackoverflow.utils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import android.content.Context;

public class DiskCache extends DiskBasedCache {
    private static final String LOG_TAG = "DISK_CACHE";

    private final WeakReference<Context> mContext;

    public DiskCache(Context context, File rootDirectory, int maxCacheSizeInBytes) {
        super(rootDirectory, maxCacheSizeInBytes);

        mContext = new WeakReference<Context>(context);
    }

    public boolean contains(String key) {
        return mEntries.containsKey(key);
    }

    @Override
    public synchronized Entry get(String key) {
        Entry entry = super.get(key);
        return entry;
    }

    public static Entry createFakeHeaderEntry(byte[] data) throws IOException {
        Entry e = new Entry();
        e.data = data;
        e.ttl = Long.MAX_VALUE;
        e.softTtl = Long.MAX_VALUE;
        return e;
    }
}
