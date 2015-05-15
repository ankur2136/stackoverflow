package com.ankur.stackoverflow.domain.usecase;

import com.ankur.stackoverflow.executor.PostExecutionThread;
import com.ankur.stackoverflow.executor.ThreadExecutor;

import java.util.concurrent.Future;

public class BaseUseCase {
    private static final String LOG_TAG = "BaseUseCase";
    protected boolean mAsync = true;
    protected boolean mApplyUserState;
    protected PostExecutionThread mPostExecutionThread;
    protected ThreadExecutor mThreadExecutor;
    protected Callback mCallback;
    protected Future mFuture;

    protected void notifyOnSuccess(final Object obj) {
        if (mPostExecutionThread != null) {
            mPostExecutionThread.post(new Runnable() {
                @Override
                public void run() {
                    if (mCallback != null) {
                        mCallback.onSuccess(obj);
                    }
                }
            });
        } else {
            if (mCallback != null) {
                mCallback.onSuccess(obj);
            }
        }
    }

    protected void notifyOnError(final Exception exception) {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                if (mCallback != null) {
                    mCallback.onError(exception);
                }
            }
        });
    }

    protected boolean isTaskRunning() {
        if (mFuture == null) {
            return false;
        }

        if (mFuture.isDone() || mFuture.isCancelled()) {
            return false;
        }

        return true;
    }
}
