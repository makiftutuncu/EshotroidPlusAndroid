package com.mehmetakiftutuncu.eshotroid.utilities;

import timber.log.Timber;

public class Log {
    public static void debug(String tag, String message, Object... args) {
        Timber.tag("[Eshotroid+] " + tag).d(message, args);
    }

    public static void warn(String tag, String message, Object... args) {
        Timber.tag("[Eshotroid+] " + tag).w(message, args);
    }

    public static void error(String tag, String message, Object... args) {
        Timber.tag("[Eshotroid+] " + tag).e(message, args);
    }

    public static void error(String tag, Throwable throwable, String message, Object... args) {
        Timber.tag("[Eshotroid+] " + tag).e(throwable, message, args);
    }
}
