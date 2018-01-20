package com.jyl.base.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.xutils.common.util.LogUtil;

/**
 * Created by yulin.jing on 2017/5/22.
 */

/**
 * desc:App先关的工具类
 * last modified time:2017/5/19 14:42
 *
 * @author yulin.jing
 * @since 2017/5/19
 */
public class ApkUtil {
    private final static String TAG = ApkUtil.class.getSimpleName();
    /**
     * 获取Environment环境
     *
     * @param context
     * @return
     */
    public static String getEnvironment(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Object object = (Object) applicationInfo.metaData.get("environment");
            return object.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取应用的版本号
     * @param mContext
     * @return
     */
    public static String getApkVersionName(Context mContext) {
        String name = "";
        try {
            PackageInfo packageInfo = mContext.getPackageManager()
                    .getPackageInfo(mContext.getPackageName(), 0);
            name = packageInfo.versionName;
            LogUtil.d("getApkVersionCode versionName:"+name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

}
