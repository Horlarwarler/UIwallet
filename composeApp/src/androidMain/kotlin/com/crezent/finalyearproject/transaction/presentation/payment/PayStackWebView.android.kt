package com.crezent.finalyearproject.transaction.presentation.payment

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.crezent.finalyearproject.core.domain.util.ApiRoute

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun PayStackWebView(
    authorizationUrl: String,
    verifyPayment: () -> Unit,
    closeDialog: () -> Unit,
) {
    AndroidView(
        factory = { ctx ->
            val webView = WebView(ctx).apply {
                settings.apply {
                    javaScriptEnabled = true
                    javaScriptCanOpenWindowsAutomatically = true
                    domStorageEnabled = true
                }
                loadUrl(authorizationUrl)
            }
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    val url = request?.url
                    println("WEBVIEW Url is $url")
                    println("WEBVIEW host is ${url?.host}")
                    println("WEBVIEW PATH IS ${url?.path}")
                    if (url?.path == "/callback") {
                        verifyPayment()
                    } else if (url?.path == "/cancel-payment") {
                       /// verifyPayment()
                        closeDialog()
                    }

                    return super.shouldOverrideUrlLoading(view, request)

                }
            }
            // webView.loadUrl(authorizationUrl)

            webView
        },
        modifier = Modifier
            .padding(
                top = 40.dp
            )
            .fillMaxSize()
    )

}