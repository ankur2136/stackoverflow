package com.ankur.stackoverflow.executor;

public interface ThreadExecutor {
    java.util.concurrent.Future<?> execute(final Runnable runnable);
}
