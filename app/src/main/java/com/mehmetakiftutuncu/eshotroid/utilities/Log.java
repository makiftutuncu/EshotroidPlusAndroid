package com.mehmetakiftutuncu.eshotroid.utilities;

public class Log {
    public static void debug(String tag, String message) {
        android.util.Log.d("[" + tag + "]", message);
    }

    public static void error(String tag, String message) {
        android.util.Log.e("[" + tag + "]", message);
    }

    public static void error(String tag, String message, Throwable error) {
        android.util.Log.e("[" + tag + "]", message, error);
    }
}
