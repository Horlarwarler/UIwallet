package com.crezent.finalyearproject.transaction.presentation.payment

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL
import platform.Foundation.NSURLComponents
import platform.Foundation.NSURLRequest
import platform.UIKit.UIViewController
import platform.WebKit.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import io.ktor.client.engine.darwin.KtorNSURLSessionDelegate
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PayStackWebView(
    authorizationUrl: String,
    verifyPayment: () -> Unit,
    closeDialog: () -> Unit,
) {
    val wkNavigationDelegate = object : NSObject(), WKNavigationDelegateProtocol {
        override fun webView(
            webView: WKWebView,
            decidePolicyForNavigationAction: WKNavigationAction,
            decisionHandler: (WKNavigationActionPolicy) -> Unit
        ) {
            val url = decidePolicyForNavigationAction.request.URL?.absoluteString
            println("WEBVIEW Url is: $url") // Debugging: Print the URL

            if (url != null) {
                // Normalize the URL by removing double slashes
                val normalizedUrl = url.replace("//", "/")

                when {
                    normalizedUrl.contains("/callback") -> {
                        println("Callback URL detected, verifying payment...")
                        verifyPayment()
                        decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyCancel)
                        return
                    }

                    normalizedUrl.contains("/cancel-payment") -> {
                        println("Cancel URL detected, closing dialog...")
                        closeDialog()
                        decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyCancel)
                        return
                    }
                }
            } else {
                println("URL is null") // Debugging: Handle null URL case
            }

            decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyAllow)
        }

    }

    UIKitView(
        factory = {
            val webView = WKWebView()
            webView.navigationDelegate = wkNavigationDelegate
            webView.loadRequest(NSURLRequest.requestWithURL(NSURL.URLWithString(authorizationUrl)!!))
            webView
        },
        modifier = Modifier
            .padding(top = 40.dp)
            .fillMaxSize(),
        interactive = true
    )
}
