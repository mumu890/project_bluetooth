package com.jyl.base.util;

import android.widget.Toast;

import com.jyl.base.app.BaseApp;


/**
 * desc:
 * last modified time:2017/5/27 14:26
 *
 * @author yulin.jing
 * @since 2017/5/27
 */
public class ToastUtil {

    public static void showShort(int msg){
        Toast.makeText(BaseApp.context,msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(int msg){
        Toast.makeText(BaseApp.context,msg, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(String msg){
        Toast.makeText(BaseApp.context,msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(String msg){
        Toast.makeText(BaseApp.context,msg, Toast.LENGTH_SHORT).show();
    }

}
