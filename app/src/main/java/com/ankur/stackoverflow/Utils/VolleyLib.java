package com.ankur.stackoverflow.utils;

import java.io.File;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.Build;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NoCache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

public class VolleyLib {

    public static final String    LOG_TAG             = "VOLLEY_LIB";

    private static final String   DEFAULT_CACHE_DIR   = "volley";

    private static final int      MAX_MEM_CACHE       = (int) (Runtime.getRuntime().maxMemory() / 1024 / 8); // kilobytes

    private static final int      DISK_CACHE_MAX_SIZE = 20 * 1024 * 1024;                                   // bytes

    // This queue is used for making unisearch API calls only
    private static RequestQueue   sSearchRequestQueue;

    // This queue is used for downloading images and making general API calls
    private static RequestQueue   sGeneralRequestQueue;

    // This queue is used for getting authenticated urls only
    private static RequestQueue   sPlayerRequestQueue;

    // This queue is used for posting analytics messages only
    private static RequestQueue   sAnalyticsRequestQueue;

    private static MemoryCache    sMemoryCache;

    private static DiskBasedCache sDiskCache;

    private static ImageLoader    sImageLoader;

    public static void init(Context context) {
        File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);

        // TODO: new DiskCache(context, cacheDir, DISK_CACHE_MAX_SIZE);
        sDiskCache = new DiskBasedCache(cacheDir, DISK_CACHE_MAX_SIZE);

        sMemoryCache = new MemoryCache(MAX_MEM_CACHE);
        sGeneralRequestQueue = newGeneralRequestQueue();
        sSearchRequestQueue = newSearchRequestQueue();
        sPlayerRequestQueue = newPlayerRequestQueue();
        sAnalyticsRequestQueue = newAnalyticsRequestQueue();
        sImageLoader = new ImageLoader(sGeneralRequestQueue, sMemoryCache);
        LogUtils.infoLog(LOG_TAG, "Memory Cache Size: " + MAX_MEM_CACHE);
    }

    private static void setDefaultCookieManager() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
        CookieHandler.setDefault(cookieManager);
    }

    private static RequestQueue newGeneralRequestQueue() {
        RequestQueue queue = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            queue = new RequestQueue(sDiskCache, new BasicNetwork(new OkHttpStack()));
        } else {
            queue = new RequestQueue(sDiskCache, new BasicNetwork(new HurlStack()));
        }
        queue.start();
        return queue;
    }

    private static RequestQueue newSearchRequestQueue() {
        RequestQueue queue = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            queue = new RequestQueue(new NoCache(), new BasicNetwork(new OkHttpStack()), 1);
        } else {
            queue = new RequestQueue(new NoCache(), new BasicNetwork(new HurlStack()), 1);
        }
        queue.start();
        return queue;
    }

    private static RequestQueue newPlayerRequestQueue() {
        RequestQueue queue = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            queue = new RequestQueue(new NoCache(), new BasicNetwork(new OkHttpStack()), 1);
        } else {
            queue = new RequestQueue(new NoCache(), new BasicNetwork(new HurlStack()), 1);
        }
        queue.start();
        return queue;
    }

    private static RequestQueue newAnalyticsRequestQueue() {
        RequestQueue queue = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            queue = new RequestQueue(new NoCache(), new BasicNetwork(new OkHttpStack()), 1);
        } else {
            queue = new RequestQueue(new NoCache(), new BasicNetwork(new HurlStack()), 1);
        }
        queue.start();
        return queue;
    }

    public static RequestQueue getSearchRequestQueue() {
        return sSearchRequestQueue;
    }

    public static RequestQueue getRequestQueue() {
        return sGeneralRequestQueue;
    }

    public static RequestQueue getPlayerRequestQueue() {
        return sPlayerRequestQueue;
    }

    public static RequestQueue getAnalyticsRequestQueue() {
        return sAnalyticsRequestQueue;
    }

    public static ImageLoader getImageLoader() {
        return sImageLoader;
    }

    public static boolean diskCacheContains(String key) {
        return sDiskCache.mEntries.containsKey(key);
    }

    public static void trimMemory(float ratio) {
        if (ratio == 1.0f) {
            sMemoryCache.evictAll();
        } else {
            int size = sMemoryCache.size();
            sMemoryCache.trimToSize((int) (size * ratio));
        }
    }

    public static class OkHttpStack extends HurlStack {
        private final OkUrlFactory okUrlFactory;

        public OkHttpStack() {
            this(new OkUrlFactory(new OkHttpClient()));
        }

        public OkHttpStack(OkUrlFactory okUrlFactory) {
            if (okUrlFactory == null) {
                throw new NullPointerException("Client must not be null.");
            }
            this.okUrlFactory = okUrlFactory;
        }

        @Override
        protected HttpURLConnection createConnection(URL url) throws IOException {
            return okUrlFactory.open(url);
        }
    }
}
