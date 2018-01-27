package com.jyl.base.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import org.xutils.x;

/**
 * desc:
 * last modified time:2018/1/11 16:15
 *
 * @author yulin.jing
 * @since 2018/1/11
 */
public class BaseApp extends MultiDexApplication {
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public void initXutil(boolean showLog) {
        //初始化XUtils3
        x.Ext.init(this);
        //设置XUtils3 debug模式
        x.Ext.setDebug(true);
    }
}
