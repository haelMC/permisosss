package com.example.camarita.inven.webviewcamera

import android.Manifest
import android.os.Bundle
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.camarita.R

class MainActivity : AppCompatActivity() {
    private val webview: WebView by lazy { findViewById(R.id.webview) }
    private var permissionRequest: PermissionRequest? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            permissionRequest?.apply {
                if (granted) {
                    grant(arrayOf(PermissionRequest.RESOURCE_VIDEO_CAPTURE))
                } else {
                    deny()
                    Toast.makeText(
                        this@MainActivity,
                        "Permiso de cámara denegado. No se podrá acceder a la cámara.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webview.apply {
            webViewClient = WebViewClient()
            webChromeClient = object : WebChromeClient() {
                override fun onPermissionRequest(request: PermissionRequest) {
                    if (PermissionRequest.RESOURCE_VIDEO_CAPTURE in request.resources) {
                        permissionRequest = request
                        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            }

            settings.javaScriptEnabled = true
            settings.mediaPlaybackRequiresUserGesture = false
            settings.allowFileAccess = true
            settings.domStorageEnabled = true

            // URL confiable que solicita cámara
            loadUrl("https://ati.mpsr.pe/panel/")
        }
    }
}
