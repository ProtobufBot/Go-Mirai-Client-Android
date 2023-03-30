package cn.lliiooll.gmca.receiver

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import cn.lliiooll.gmca.service.GMCService
import cn.lliiooll.gmca.service.utils.checkAndroidVersion

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // 开机时
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            val intentN = Intent(context, GMCService::class.java)
            context?.bindService(intentN, object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

                }

                override fun onServiceDisconnected(name: ComponentName?) {

                }
            }, AppCompatActivity.BIND_AUTO_CREATE)
            if (checkAndroidVersion(Build.VERSION_CODES.O)) {
                context?.startForegroundService(intentN)
            } else {
                context?.startService(intentN)
            }
        }

    }
}