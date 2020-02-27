package com.seven.joker.utils;

import android.util.Log;

import com.seven.joker.BuildConfig;

public class LogUtil {
    public static boolean isDebug = BuildConfig.DEBUG;

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }
}
