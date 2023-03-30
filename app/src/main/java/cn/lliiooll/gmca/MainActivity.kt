package cn.lliiooll.gmca

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import cn.lliiooll.gmca.databinding.ActivityMainBinding
import cn.lliiooll.gmca.service.GMCService
import cn.lliiooll.gmca.service.utils.JavaUtils
import cn.lliiooll.gmca.service.utils.checkAndroidVersion
import cn.lliiooll.gmca.service.utils.checkNotifyPermission
import cn.lliiooll.gmca.service.utils.checkPermissions
import cn.lliiooll.gmca.service.utils.isInBatteryWhiteList
import cn.lliiooll.gmca.service.utils.makeToastLong
import cn.lliiooll.gmca.service.utils.makeToastSort
import cn.lliiooll.gmca.service.utils.requestJoinBatteryWhiteList
import cn.lliiooll.gmca.service.utils.requestNotifyPermission
import cn.lliiooll.gmca.service.utils.requestPermissions
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var binding: ActivityMainBinding
    private val REQUEST_CODE = 0x3c;
    private val NOTIFY_REQUEST_CODE = 0x3d;
    private val BATTERT_REQUEST_CODE = 0x3e;
    private val GMC_URL = "http://localhost:9000/";
    private val permissions = arrayListOf(
        Manifest.permission.RECEIVE_BOOT_COMPLETED,
        Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
        Manifest.permission.INTERNET
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.webView = binding.gmcContent

        if (checkAndroidVersion(Build.VERSION_CODES.TIRAMISU)) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (checkAndroidVersion(Build.VERSION_CODES.P)) {
            permissions.add(Manifest.permission.FOREGROUND_SERVICE)
        }
        initGMC()
    }

    fun initWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.loadUrl(GMC_URL)
    }

    fun initGMC() {
        if (!this.checkPermissions(permissions)) {
            this.requestPermissions(REQUEST_CODE, permissions)
        } else if (!this.checkNotifyPermission()) {
            this.requestNotifyPermission(NOTIFY_REQUEST_CODE)
        } else {
            if (checkAndroidVersion(Build.VERSION_CODES.M)) {
                if (!this.isInBatteryWhiteList()) {
                    this.requestJoinBatteryWhiteList(BATTERT_REQUEST_CODE)
                } else {
                    startGMC()
                }
            } else {
                startGMC()
            }
        }

    }

    fun startGMC() {
        this.makeToastLong("GMC启动中，请稍后...")
        if (!JavaUtils.isConnected(GMC_URL)) {
            val intent = Intent(this, GMCService::class.java)
            this.bindService(intent, object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

                }

                override fun onServiceDisconnected(name: ComponentName?) {

                }
            }, BIND_AUTO_CREATE)
            if (checkAndroidVersion(Build.VERSION_CODES.O)) {
                this.startForegroundService(intent)
            } else {
                this.startService(intent)
            }
            thread {
                while (true) {
                    if (JavaUtils.isConnected(GMC_URL)) {
                        Handler(Looper.getMainLooper()).post {
                            initWebView()
                        }
                        break
                    }
                    Thread.sleep(500L)
                }
            }
        }else{
            initWebView()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (this.checkPermissions(this.permissions)) {
                this.makeToastSort("权限获取失败，应用退出...")
                this.finish()
            } else {
                initGMC()
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == NOTIFY_REQUEST_CODE) {
            if (!this.checkNotifyPermission()) {
                this.makeToastSort("通知权限获取失败，应用退出...")
                this.finish()
            } else {
                initGMC()
            }
        }
        if (requestCode == BATTERT_REQUEST_CODE) {
            if (!this.isInBatteryWhiteList()) {
                this.makeToastSort("加入电池优化失败，应用退出...")
                this.finish()
            } else {
                initGMC()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}