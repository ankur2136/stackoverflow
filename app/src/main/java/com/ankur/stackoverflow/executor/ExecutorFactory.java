package com.ankur.stackoverflow.executor;

public class ExecutorFactory {
    private static ThreadExecutor      sThreadExecutor      = new JobExecutor();

    private static PostExecutionThread sPostExecutionThread = new UIThread();

    public static ThreadExecutor getThreadExecutorInstance() {
        return sThreadExecutor;
    }

    public static PostExecutionThread getPostExecutionThreadInstance() {
        return sPostExecutionThread;
    }

}
