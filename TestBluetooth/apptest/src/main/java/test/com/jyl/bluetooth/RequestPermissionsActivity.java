package test.com.jyl.bluetooth;

import android.Manifest;
import android.app.Activity;

import com.jyl.base.ui.RequestPermissionsActivityBase;

/**
 * Created by lzk on 2017/10/16.
 */

public class RequestPermissionsActivity extends RequestPermissionsActivityBase {

    private static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected String[] getRequiredPermissions() {
        return REQUIRED_PERMISSIONS;
    }

    @Override
    protected String[] getDesiredPermissions() {
        return REQUIRED_PERMISSIONS;
    }

    // 返回true 没有申请到权限
    public static boolean startPermissionActivity(Activity activity) {
        return startPermissionActivity(activity,
                REQUIRED_PERMISSIONS,
                RequestPermissionsActivity.class);
    }
}