package com.jyl.base.util;

import android.os.Environment;

import java.io.File;
import java.text.DecimalFormat;

/**
 * desc:
 * last modified time:2017/5/25 19:41
 *
 * @author yulin.jing
 * @since 2017/5/25
 */
public class FileUtil {

    /**
     * 检查SD卡是否可用
     */
    public static boolean isExistSDcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 文件夹若不存在则,创建路径
     */
    public static void isExist(String filePath) {
        File file = new File(filePath);
        if (isExistSDcard()) {
            if (!file.exists()) {
                file.mkdir();
            }
        }
    }

    /**
     * 查看SD卡内文件是否存在
     */
    public static boolean exists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 删除文件
     *
     * @param filePath
     * @return
     */
    public static boolean delFiel(String filePath) {
        if (exists(filePath)) {
            return new File(filePath).delete();
        }
        return false;
    }

    /**
     * 检查SD卡是否存在指定文件
     *
     * @param fileName
     * @return 存在，返回图片绝对路径
     */
    public static String getFileUri(String filePath, String fileName) {
        File file = new File(filePath + File.separator, fileName);
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        return null;
    }

    /**
     * 从url获取文件名(hashCode方式)
     *
     * @param urlString
     * @return
     */
    public static String getFileNameFromUrlByHashCode(String urlString) {
        int firstHalfLength = urlString.length() / 2;
        String localFilename = String.valueOf(urlString.substring(0, firstHalfLength).hashCode());
        localFilename += String.valueOf(urlString.substring(firstHalfLength).hashCode());
        return localFilename;
    }

    /**
     * 格式化文件大小(kb单位)
     * @param size
     * @return
     */
    public static String formatFileSizeKB(float size) {
        size = size *1024;
        DecimalFormat format = new DecimalFormat("###,###,##0.00");
        if (size < 1024) {
            format.applyPattern("###,###,##0.00B");
        } else if (size >= 1024 && size < 1024 * 1024) {
            size /= 1024;
            format.applyPattern("###,###,##0.00KB");
        } else if (size >= 1024 * 1024 && size < 1024 * 1024 * 1024) {
            size /= (1024 * 1024);
            format.applyPattern("###,###,##0.00MB");
        } else if (size >= 1024 * 1024 * 1024 && size < 1024 * 1024 * 1024 * 1024) {
            size /= (1024 * 1024 * 1024);
            format.applyPattern("###,###,##0.00GB");
        } else if (size >= 1024 * 1024 * 1024 * 1024 && size < 1024 * 1024 * 1024 * 1024 * 1024) {
            size /= (1024 * 1024 * 1024 * 1024);
            format.applyPattern("###,###,##0.00GB");
        }
        return format.format(size);
    }

    /**
     * 格式化文件大小（b单位）
     * @param size
     * @return
     */
    public static String formatFileSizeB(float size) {
        DecimalFormat format = new DecimalFormat("###,###,##0.00");
        if (size < 1024) {
            format.applyPattern("###,###,##0.00B");
        } else if (size >= 1024 && size < 1024 * 1024) {
            size /= 1024;
            format.applyPattern("###,###,##0.00KB");
        } else if (size >= 1024 * 1024 && size < 1024 * 1024 * 1024) {
            size /= (1024 * 1024);
            format.applyPattern("###,###,##0.00MB");
        } else if (size >= 1024 * 1024 * 1024 && size < 1024 * 1024 * 1024 * 1024) {
            size /= (1024 * 1024 * 1024);
            format.applyPattern("###,###,##0.00GB");
        } else if (size >= 1024 * 1024 * 1024 * 1024 && size < 1024 * 1024 * 1024 * 1024 * 1024) {
            size /= (1024 * 1024 * 1024 * 1024);
            format.applyPattern("###,###,##0.00GB");
        }
        return format.format(size);
    }
}
