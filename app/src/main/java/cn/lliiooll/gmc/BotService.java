package cn.lliiooll.gmc;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;
import androidx.annotation.Nullable;
import cn.lliiooll.gmc.util.NotificationUtil;
import gmc.Gmc;
import net.lz1998.gomiraiclient.MainActivity;
import org.jetbrains.annotations.NotNull;

/**
 * 用来运行gmc的服务
 */
public class BotService extends Service {
    private static boolean active = false;

    @Override
    public void onCreate() {
        new Thread(Gmc::start).start();// 新建线程启动gmc
        Toast.makeText(this, "GMC启动中...", Toast.LENGTH_LONG).show();// 发个通知别让用户以为程序卡了
        active = true;
        startForeground(1, NotificationUtil.create(this));// 启动前台服务
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        active = false;
        stopForeground(true);// 停止前台服务
        super.onDestroy();
    }

    @NotNull
    public static boolean isActive() {
        return BotService.active;
    }

    public static void start(@NotNull MainActivity activity) {
        Intent intent = new Intent(activity, BotService.class);
        activity.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, BIND_AUTO_CREATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.startForegroundService(intent);
        } else {
            activity.startService(intent);
        }
    }
}
