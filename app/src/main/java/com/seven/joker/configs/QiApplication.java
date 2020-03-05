package com.seven.joker.configs;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class QiApplication extends Application {
    public static final String BUGLY_ID = "ffc2b03248";
    private static QiApplication instance;

    public static QiApplication getInstance() {
        if (instance == null) {
            try {
                Method method = Class.forName("android.app.ActivityThread").getDeclaredMethod("currentApplication");
                instance = (QiApplication) method.invoke(null, new  Object[]{});
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化友盟
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        //初始化bugly
        CrashReport.initCrashReport(getApplicationContext(), BUGLY_ID, false);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //热修复 Multidex.install(this);
    }
}
