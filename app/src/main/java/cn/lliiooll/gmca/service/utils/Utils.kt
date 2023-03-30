package cn.lliiooll.gmca.service.utils

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.renderscript.RenderScript.Priority
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import cn.lliiooll.gmca.R
import java.io.File

class Utils {
}

fun Context.checkNotifyPermission(): Boolean {
    return NotificationManagerCompat.from(this).areNotificationsEnabled()
}

fun Context.isInBatteryWhiteList(): Boolean {
    return this.getSystemService(PowerManager::class.java)
        .isIgnoringBatteryOptimizations(this.packageName)
}

fun Context.checkPermissions(vararg permissions: String): Boolean {
    return this.checkPermissions(permissions.toMutableList())
}

fun Context.checkPermissions(permissions: MutableList<String>): Boolean {
    var has = true
    for (permission in permissions) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            has = false
            break
        }
    }
    return has
}

fun Activity.requestPermissions(code: Int, permissions: MutableList<String>) {
    this.requestPermissions(code, *permissions.toTypedArray())
}

fun Activity.requestPermissions(code: Int, vararg permissions: String) {
    this.makeToastLong("请授予必要的权限避免GMC被后台杀死")
    ActivityCompat.requestPermissions(this, permissions, code)
}

fun Activity.requestNotifyPermission(code: Int) {
    this.makeToastLong("请开启通知权限避免GMC被后台杀死")
    try {
        val intent = Intent()
        intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, this.packageName)
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, this.applicationInfo.uid)
        startActivityForResult(intent, code)
    } catch (e: Throwable) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.putExtra("package", this.packageName)
        startActivityForResult(intent, code)
    }
}

fun checkAndroidVersion(code: Int): Boolean {
    return Build.VERSION.SDK_INT >= code;
}

fun Context.makeToastSort(text: CharSequence) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.makeToastLong(text: CharSequence) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun Activity.requestJoinBatteryWhiteList(code: Int) {
    this.makeToastLong("请授予GMC电池优化白名单避免GMC被后台杀死")
    val intent = Intent()
    intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
    intent.data = Uri.parse("package:$packageName")
    startActivityForResult(intent, code)
}

fun File.checkOrMkdirs(): Boolean {
    if (!this.exists()) {
        return this.mkdirs()
    }
    return true
}

fun Context.buildNotify(name: String, desc: String, channel: String): Notification {
    if (checkAndroidVersion(Build.VERSION_CODES.O)) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channel, name, importance)
        channel.description = desc
        val notifyManager = this.getSystemService(NotificationManager::class.java)
        notifyManager.createNotificationChannel(channel)
    }
    val intent = Intent(this, this.javaClass)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    return NotificationCompat.Builder(this, channel)
        .setContentText(desc)
        .setContentTitle(name)
        .setContentIntent(pendingIntent)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()
}