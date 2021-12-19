package cn.lliiooll.gmc.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import cn.lliiooll.gmc.BotService;
import net.lz1998.gomiraiclient.MainActivity;
import net.lz1998.gomiraiclient.R;

public class NotificationUtil {
    public static Notification create(BotService service) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "GMC";
            String description = "GMC";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("gmc_channel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = service.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        Intent intent = new Intent(service, BotService.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(service, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(service, "gmc_channel")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("GMC")
                .setContentText("Start")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //创建通知并返回


        return builder.build();
    }

}
