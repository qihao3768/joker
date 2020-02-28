package com.seven.joker.utils;

import android.util.Log;

import com.seven.joker.BuildConfig;

/*
log工具类，只在debug模式下显示日志
 */
public class LogUtil {
    private static boolean isDebug = BuildConfig.DEBUG;
    private final static String tag = "qihao";

    public static void print(String type, String msg) {
        if (isDebug) {
            switch (type){
                case "d":
                    Log.d(tag, msg);
                    break;
                case "e":
                    Log.d(tag, msg);
                    break;
                case "i":
                    Log.d(tag, msg);
                    break;
            }

        }
    }
}
