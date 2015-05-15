package com.ankur.stackoverflow.common;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

public class AsyncUtils {

    public static void executeAsyncTask(final Runnable runnable, boolean runOnExecutor) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                runnable.run();
                return null;
            }
        };

        if (runOnExecutor) {
            CompatUtils.executeOnExecutor(task);
        } else {
            task.execute();
        }
    }

    public static void runOnUiThread(final Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            new Handler(Looper.getMainLooper()).post(runnable);
        }
    }
}
