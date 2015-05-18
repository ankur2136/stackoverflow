package com.ankur.stackoverflow.utils;

import java.util.Locale;
import android.util.Log;

public class LogUtils {
    private static final String LOG_PREFIX = "[StackOverflow]";

    public static void verboseLog(String tag, String message) {
        verboseLog(tag, message, null);
    }

    private static Logger       sLogger;

    private static final Logger DEFAULT_LOGGER = new DebugLogger();

    public static void verboseLog(String tag, String message, Exception ex) {
        getLogger().verbose(tag, message, ex);
    }

    public static void debugLog(String tag, String message) {
        getLogger().debug(tag, message);
    }

    public static void debugLog(String tag, String message, Exception ex) {
        getLogger().verbose(tag, message, ex);
    }

    public static void infoLog(String tag, String message) {
        getLogger().info(tag, message);
    }

    public static void infoLog(String tag, String message, Exception ex) {
        getLogger().info(tag, message, ex);
    }

    public static void warnLog(String tag, String message) {
        getLogger().warn(tag, message);
    }

    public static void warnLog(String tag, String message, Exception ex) {
        getLogger().warn(tag, message, ex);
    }

    public static void errorLog(String tag, String message) {
        getLogger().error(tag, message);
    }

    public static void errorLog(String tag, String message, Exception ex) {
        getLogger().error(tag, message, ex);
    }

    public static boolean isDebugLogEnabled() {
        return true;
    }

    public static void setLogger(Logger logger) {
        sLogger = logger;
    }

    private static Logger getLogger() {
        if (sLogger == null) {
            return DEFAULT_LOGGER;
        } else {
            return sLogger;
        }
    }

    public static interface Logger {
        void verbose(String tag, String message);

        void verbose(String tag, String message, Throwable exception);

        void debug(String tag, String message);

        void debug(String tag, String message, Throwable exception);

        void info(String tag, String message);

        void info(String tag, String message, Throwable exception);

        void warn(String tag, String message);

        void warn(String tag, String message, Throwable exception);

        void error(String tag, String message);

        void error(String tag, String message, Throwable exception);
    }

    public static abstract class NoOpLogger implements Logger {
        @Override
        public void verbose(String tag, String message) {
            verbose(tag, message, null);
        }

        @Override
        public void verbose(String tag, String message, Throwable exception) {

        }

        @Override
        public void debug(String tag, String message) {
            debugLog(tag, message, null);
        }

        @Override
        public void debug(String tag, String message, Throwable exception) {

        }

        @Override
        public void info(String tag, String message) {
            info(tag, message, null);
        }

        @Override
        public void info(String tag, String message, Throwable exception) {

        }

        @Override
        public void warn(String tag, String message) {
            warn(tag, message, null);
        }

        @Override
        public void warn(String tag, String message, Throwable exception) {

        }

        @Override
        public void error(String tag, String message) {
            error(tag, message, null);
        }

        @Override
        public void error(String tag, String message, Throwable exception) {

        }
    }

    public static class DebugLogger extends NoOpLogger {
        @Override
        public void verbose(String tag, String message, Throwable exception) {
            log(Log.VERBOSE, tag, message, exception);
        }

        @Override
        public void debug(String tag, String message, Throwable exception) {
            log(Log.DEBUG, tag, message, exception);
        }

        @Override
        public void info(String tag, String message, Throwable exception) {
            log(Log.INFO, tag, message, exception);
        }

        @Override
        public void warn(String tag, String message, Throwable exception) {
            log(Log.WARN, tag, message, exception);
        }

        @Override
        public void error(String tag, String message, Throwable exception) {
            log(Log.ERROR, tag, message, exception);
        }
    }

    private static void log(int priority, String tag, String message, Throwable exception) {
        if (message == null || message.length() == 0) {
            if (exception != null) {
                message = Log.getStackTraceString(exception);
            } else {
                // Swallow message if it's null and there's no throwable.
                return;
            }
        } else if (exception != null) {
            message += "\n" + Log.getStackTraceString(exception);
        }

        if (message.length() < 4000) {
            Log.println(priority, getCaller(tag), message);
        } else {
            // It's rare that the message will be this large, so we're ok
            // with the perf hit of splitting
            // and calling Log.println N times. It's possible but unlikely
            // that a single line will be
            // longer than 4000 characters: we're explicitly ignoring this
            // case here.
            String[] lines = message.split("\n");
            for (String line : lines) {
                Log.println(priority, getCaller(tag), line);
            }
        }
    }

    private static String getCaller(String tag) {
        String caller = "<unknown>";
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();
        if (trace.length < 6) {
            return caller;
        }
        // Walk up the stack looking for the first caller outside of LogUtils.
        // It will be at least 5 frames up, so start there.
        for (int i = 5; i < trace.length; i++) {
            // Class<?> clazz = trace[i].getClass();
            String clazzName = trace[i].getClassName();
            if (!clazzName.contains("Log")) {
                String callingClass = clazzName;
                callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
                callingClass = callingClass.substring(callingClass.lastIndexOf('$') + 1);
                caller = callingClass + "." + trace[i].getMethodName();
                break;
            }
        }
        return String.format(Locale.US, "[%d/%s] %s %s: %s", Thread.currentThread().getId(), Thread.currentThread()
                .getName(), LOG_PREFIX, tag, caller);
    }

}
