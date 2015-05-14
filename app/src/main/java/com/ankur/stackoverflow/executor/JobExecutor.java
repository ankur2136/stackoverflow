package com.ankur.stackoverflow.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.Process;

public class JobExecutor implements ThreadExecutor {
    private static final int              CPU_COUNT            = Runtime.getRuntime().availableProcessors();

    private static final int              CORE_POOL_SIZE       = CPU_COUNT + 1;

    private static final int              MAX_POOL_SIZE        = CPU_COUNT * 2 + 1;

    // Sets the amount of time an idle thread waits before terminating
    private static final int              KEEP_ALIVE_TIME      = 10;

    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private final BlockingQueue<Runnable> mWorkQueue;

    private final ThreadPoolExecutor mThreadPoolExecutor;

    private final ThreadFactory mThreadFactory;

    public JobExecutor() {
        this.mWorkQueue = new LinkedBlockingQueue<>();
        this.mThreadFactory = new JobThreadFactory();
        this.mThreadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT, this.mWorkQueue, this.mThreadFactory);
    }

    @Override
    public java.util.concurrent.Future<?> execute(Runnable runnable) {
        if (runnable == null) {
            throw new IllegalArgumentException("Runnable to execute cannot be null");
        }
        return this.mThreadPoolExecutor.submit(runnable);
    }

    private static class JobThreadFactory implements ThreadFactory {
        private static final String THREAD_NAME = "JOB_";

        private final AtomicInteger mCount      = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, THREAD_NAME + mCount.incrementAndGet());
            thread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
            return thread;
        }
    }
}