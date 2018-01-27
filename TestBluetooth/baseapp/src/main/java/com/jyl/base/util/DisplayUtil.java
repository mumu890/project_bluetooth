package com.jyl.base.util;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * desc:屏幕工具类
 * last modified time:2017/5/19 14:42
 *
 * @author yulin.jing
 * @since 2017/5/19
 */
public class DisplayUtil {

    public static int dip2sp(Context context, float dipValue) {
        int pxValue = dip2px(context, dipValue);
        return px2sp(context, pxValue);
    }

    public static int sp2dip(Context context, float spValue) {
        int pxValue = sp2px(context, spValue);
        return px2dip(context, pxValue);
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变  【代码里最终设置的单位基本都是px】
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变  【代码里最终设置的单位基本都是px，所以最常用使用这个】
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }



    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }
        //获取屏幕的高度
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }
    /**
     * 获取状态栏高度
     * @return
     */
    public static int getStatusBarHeight(Context context){
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //判断是否有smartbar(底部菜单栏)
    public static boolean hasSmartBar(){
        try {
            Method method = Class.forName("android.os.Build").getMethod("hasSmartBar");
            return ((Boolean)method.invoke(null)).booleanValue();
        }catch (Exception e){
            if(Build.DEVICE.equals("mx2")){
                return true;
            }else if(Build.DEVICE.equals("mx")|| Build.DEVICE.equals("m9")){
                return false;
            }
            return false;
        }
    }

    public static int getSmartBarHeight(Context context){
        Log.d("smartbar:", "hassmart:" + hasSmartBar());
        return hasSmartBar()==true?DisplayUtil.px2dip(context,48):0;
    }
}
