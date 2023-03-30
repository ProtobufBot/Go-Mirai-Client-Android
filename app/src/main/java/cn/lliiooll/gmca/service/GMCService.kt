package cn.lliiooll.gmca.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import cn.lliiooll.gmca.service.utils.buildNotify
import cn.lliiooll.gmca.service.utils.checkOrMkdirs
import gmc_android.Gmc_android
import kotlin.concurrent.thread

class GMCService : Service() {
    override fun onBind(intent: Intent?): IBinder? {

        return null
    }

    override fun onCreate() {
        val runDir = getExternalFilesDir("gmcRun")
        runDir?.checkOrMkdirs()
        Gmc_android.chdir(runDir?.absolutePath)
        Gmc_android.setLogger {
            //TODO: 日志转储
        }
        thread {
            Gmc_android.start()
        }
        startForeground(1, this.buildNotify("GMC", "GMC运行状态", "gmc_channel"))
    }

    override fun onDestroy() {
        stopForeground(true)
        super.onDestroy()
    }
}