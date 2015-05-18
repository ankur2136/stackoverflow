package com.ankur.stackoverflow.utils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.Build;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

public class VolleyLib {

    public static final String    LOG_TAG             = "VOLLEY_LIB";

    private static final String   DEFAULT_CACHE_DIR   = "volley";

    private static final int      MAX_MEM_CACHE       = (int) (Runtime.getRuntime().maxMemory() / 1024 / 8); // kilobytes

    private static final int      DISK_CACHE_MAX_SIZE = 20 * 1024 * 1024;                                   // bytes

    private static RequestQueue   sGeneralRequestQueue;

    private static DiskBasedCache sDiskCache;

    public static void init(Context context) {
        File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);

        sDiskCache = new DiskBasedCache(cacheDir, DISK_CACHE_MAX_SIZE);

        sGeneralRequestQueue = newGeneralRequestQueue();
        LogUtils.infoLog(LOG_TAG, "Memory Cache Size: " + MAX_MEM_CACHE);
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

    public static RequestQueue getRequestQueue() {
        return sGeneralRequestQueue;
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
