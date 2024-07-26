package com.example.browser

import android.os.Bundle
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.webkit.WebView
import com.example.browser.R

class MainActivity : AppCompatActivity() {

    private lateinit var urlEditText: EditText
    private lateinit var tabsButton: Button
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        urlEditText = findViewById(R.id.urlEditText)
        tabsButton = findViewById(R.id.tabsButton)
        webView = findViewById(R.id.webView)

        // Set up WebView
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true

        // Set up URL input
        urlEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_GO) {
                loadUrl()
                true
            } else {
                false
            }
        }

        // Set up tabs button (placeholder for now)
        tabsButton.setOnClickListener {
            // TODO: Implement tabs functionality
        }
    }

    private fun loadUrl() {
        val url = urlEditText.text.toString()
        if (url.isNotEmpty()) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                webView.loadUrl("https://$url")
            } else {
                webView.loadUrl(url)
            }
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}