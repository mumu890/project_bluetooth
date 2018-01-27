package com.example.sofia.myapplication;

import android.content.Context;

import com.jyl.base.app.BaseApp;

/**
 * desc:
 * last modified time:2018/1/24 16:01
 *
 * @author yulin.jing
 * @since 2018/1/24
 */
public class App extends BaseApp {
    public static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initXutil(true);
    }
}
