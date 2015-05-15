package com.ankur.stackoverflow.jobs;

import com.ankur.stackoverflow.utils.LogUtils;
import com.path.android.jobqueue.log.CustomLogger;

public class JobManagerLogger implements CustomLogger {

    private static final String TAG = "JOB_MANAGER_LOGGER";

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public void d(String text, Object... args) {
        LogUtils.debugLog(TAG, String.format(text, args));
    }

    @Override
    public void e(Throwable t, String text, Object... args) {
        LogUtils.errorLog(TAG, String.format(text, args));
    }

    @Override
    public void e(String text, Object... args) {
        LogUtils.errorLog(TAG, String.format(text, args));
    }
}
