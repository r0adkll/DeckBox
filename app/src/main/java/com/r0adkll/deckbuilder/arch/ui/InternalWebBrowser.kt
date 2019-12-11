package com.r0adkll.deckbuilder.arch.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.ftinc.kit.arch.presentation.BaseActivity
import com.ftinc.kit.util.bindInt
import com.ftinc.kit.util.bindString
import com.r0adkll.deckbuilder.R
import kotlinx.android.synthetic.main.activity_internal_browser.*

class InternalWebBrowser : BaseActivity() {

    private val titleResId by bindInt(EXTRA_TITLE)
    private val url by bindString(EXTRA_URL)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_internal_browser)

        supportActionBar?.setTitle(titleResId)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appbar?.setNavigationOnClickListener {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                finish()
            }
        }

        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }
        }

        webView.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                loadingIndicator.isVisible = newProgress in (1..99)
            }
        }

        webView.loadUrl(url)
    }

    override fun setupComponent() {
    }

    companion object {
        private const val EXTRA_TITLE = "InternalWebBrowser.Title"
        private const val EXTRA_URL = "InternalWebBrowser.Url"

        fun show(context: Context, @StringRes titleResId: Int, url: String) {
            val intent = Intent(context, InternalWebBrowser::class.java).apply {
                putExtra(EXTRA_TITLE, titleResId)
                putExtra(EXTRA_URL, url)
            }
            context.startActivity(intent)
        }
    }
}
