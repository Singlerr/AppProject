package io.github.eh.eh

import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_auth.*
import java.io.BufferedReader

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        authView.apply {
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            webChromeClient = ChromeClient()
            webViewClient = IWebViewClient()
        }
        Log.e("dd", "dd")

        authView.loadData(readHTML(), "text/html", "UTF8")
        authView.addJavascriptInterface(JavascriptAuthBridge(this), "AndroidBridge")
    }

    private fun readHTML(): String {
        return resources.openRawResource(R.raw.auth).bufferedReader().use(BufferedReader::readText)
    }

    private inner class ChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
        }
    }

    private class IWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            val url = request?.url.toString()
            view?.loadUrl(url)
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            handler?.proceed()
            super.onReceivedSslError(view, handler, error)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        authView.apply {
            if ((keyCode == KeyEvent.KEYCODE_BACK) && this.canGoBack()) {
                this.goBack()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        fun starterIntent(context: Context, url: String): Intent {
            return Intent(context, JavascriptAuthBridge::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra("webview_url", url)
            }
        }
    }
}