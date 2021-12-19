package cn.lliiooll.gmc.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import org.jetbrains.annotations.NotNull;

/**
 * 权限类
 */
public class PermissionUtil {
    /**
     * 所有程序用到的权限
     */
    private static final String[] permissions = new String[]{
//            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.INTERNET,
    };
    /**
     * 权限请求id
     */
    private static final int requestCode = 0x9081;

    /**
     * 检查程序是否有需要的权限
     * @param activity 界面
     * @return 是否拥有全部权限
     */
    @NotNull
    public static boolean checkAll(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 请求权限
     * @param activity 界面
     */
    public static void request(Activity activity) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * 判断是不是程序的requestCode
     * @param requestCode
     * @return
     */
    public static boolean isSelfRequest(int requestCode) {
        return requestCode == PermissionUtil.requestCode;
    }
}
