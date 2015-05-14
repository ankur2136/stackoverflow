package com.ankur.stackoverflow.executor;

import android.os.Handler;
import android.os.Looper;

public class UIThread implements PostExecutionThread {
    private final Handler mHandler;

    public UIThread() {
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(Runnable runnable) {
        mHandler.post(runnable);
    }
}
