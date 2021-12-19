package cn.lliiooll.gmc.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import net.lz1998.gomiraiclient.MainActivity;
import org.jetbrains.annotations.NotNull;

public class PermissionUtil {
    private static final String[] permissions = new String[]{
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.INTERNET,
    };
    private static final int requestCode = 0x9081;

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

    public static void request(Activity activity) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    public static boolean isSelfRequest(int requestCode) {
        return requestCode == PermissionUtil.requestCode;
    }
}
