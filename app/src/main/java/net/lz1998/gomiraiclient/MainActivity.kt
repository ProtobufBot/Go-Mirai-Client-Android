package net.lz1998.gomiraiclient

import android.content.pm.PackageManager
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.lliiooll.gmc.BotService
import cn.lliiooll.gmc.util.PermissionUtil
import net.lz1998.gomiraiclient.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!PermissionUtil.checkAll(this)) {
            PermissionUtil.request(this)
        }
        // 启动服务
        if (!BotService.isActive()) {
            BotService.start(this)
        }
        val t = this;
        thread {
            Thread.sleep(3000L)// 等3秒让gmc启动
            t.runOnUiThread {
                binding.webview.settings.javaScriptEnabled = true
                binding.webview.settings.loadWithOverviewMode = true
                binding.webview.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView, url: String) {

                        // Inject CSS when page is done loading
                        view.loadUrl("javascript:document.querySelector(\"#root > div > section > header\").hidden=true")
                        super.onPageFinished(view, url)
                    }
                }
                binding.webview.loadUrl("http://localhost:9000/")
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (PermissionUtil.isSelfRequest(requestCode)) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is granted. Continue the action or workflow
                // in your app.
            } else {
                // Explain to the user that the feature is unavailable because
                // the features requires a permission that the user has denied.
                // At the same time, respect the user's decision. Don't link to
                // system settings in an effort to convince the user to change
                // their decision.
            }


        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}