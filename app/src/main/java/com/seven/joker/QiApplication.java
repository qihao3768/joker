package com.seven.joker;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

public class QiApplication extends Application {
    public static final String BUGLY_ID = "ffc2b03248";
    private static QiApplication instance;

    public static QiApplication getInstance() {
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
