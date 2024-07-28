package com.prashant.browser

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.webkit.WebView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.debugInspectorInfo
import com.prashant.browser.R

class MainActivity : AppCompatActivity() {

    private lateinit var urlEditText: EditText
    private lateinit var tabsButton: Button
    private lateinit var webView: WebView
    private val openTabs = mutableListOf<String>()
    private var currentTabIndex = 0

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
            val intent = Intent(this, TabsActivity::class.java)
            Log.d("TabsActivity", "Tabs list: $openTabs")
            intent.putStringArrayListExtra("TABS_LIST", ArrayList(openTabs))
            resultLauncher.launch(intent)
        }

        if (intent.getBooleanExtra("NEW_TAB", false)) {
            openTabs.add("")
            currentTabIndex = openTabs.size - 1
        }
    }

    private fun loadUrl() {
        val url = urlEditText.text.toString()
        if (url.isNotEmpty()) {
            val fullUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
                "https://$url"
            } else {
                url
            }
            webView.loadUrl(fullUrl)
            if (openTabs.size <= currentTabIndex) {
                openTabs.add(fullUrl)
            } else {
                openTabs[currentTabIndex] = fullUrl
            }
        }
    }

        var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.getIntExtra("SELECTED_TAB", -1)?.let { selectedTab ->
                if (selectedTab != -1 && selectedTab < openTabs.size) {
                    currentTabIndex = selectedTab
                    webView.loadUrl(openTabs[currentTabIndex])
                }
            }
            result.data?.getIntExtra("CLOSED_TAB", -1)?.let { closedTab ->
                if (closedTab != -1 && closedTab < openTabs.size) {
                    openTabs.removeAt(closedTab)
                    if (openTabs.isEmpty()) {
                        finish()
                    } else {
                        currentTabIndex = 0
                        webView.loadUrl(openTabs[currentTabIndex])
                    }
                }
            }
        }
    }

    companion object {
        private const val TABS_REQUEST_CODE = 1001
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}