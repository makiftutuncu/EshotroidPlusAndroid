package com.mehmetakiftutuncu.eshotroid.utilities;

public class Log {
    public static void debug(String tag, String message) {
        android.util.Log.d("[Eshotroid+] [" + tag + "]", message);
    }

    public static void warn(String tag, String message) {
        android.util.Log.w("[Eshotroid+] [" + tag + "]", message);
    }

    public static void error(String tag, String message) {
        android.util.Log.e("[Eshotroid+] [" + tag + "]", message);
    }

    public static void error(String tag, String message, Throwable error) {
        android.util.Log.e("[Eshotroid+] [" + tag + "]", message, error);
    }
}
