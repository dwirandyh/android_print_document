package com.project.printandroid

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*

class PrintHtmlActivity : AppCompatActivity() {

    private var mWebView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_print.setOnClickListener {
            doWebViewPrint()
        }
    }

    private fun doWebViewPrint() {
        val webView = WebView(this)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
                return false
            }

            override fun onPageFinished(view: WebView, url: String?) {
                Log.i("PrintHtmlActivity", "page finished loading url")
                createWebPrintJob(view)
                mWebView = null
            }
        }

//        generateHtmlDocumentFromUrl(webView, "http://developer.android.com/about/index.html")
//        generateHtmlDocument(webView)
        generateHtmlDocumentIncludeAsset(webView)
    }

    private fun generateHtmlDocument(webView: WebView) {
        // generate html document from plain text
        val htmlDocument =
            "<html><body><h1>Test Content</h1><p>Testing, testing, testing...</p></body></html>"
        webView.loadDataWithBaseURL(
            null,
            htmlDocument,
            "text/HTML",
            "UTF-8",
            null
        )

        mWebView = webView
    }

    private fun generateHtmlDocumentFromUrl(webView: WebView, url: String) {
        webView.loadUrl(url)
        mWebView = webView
    }

    private fun generateHtmlDocumentIncludeAsset(webView: WebView) {
        val htmlDocument =
            "<html>" +
                    "<body>" +
                    "<h1>Test Content</h1>" +
                    "<p>Testing, testing, testing...</p>" +
                    "<img src='./android_logo.png'>" +
                    "</body>" +
                    "</html>"
        webView.loadDataWithBaseURL(
            "file:///android_asset/images/",
            htmlDocument,
            "text/HTML",
            "UTF-8",
            null
        )

        mWebView = webView

    }

    private fun createWebPrintJob(webView: WebView) {
        // Get a PrintManager instance
        (getSystemService(Context.PRINT_SERVICE) as? PrintManager)?.let { printManager ->

            val jobName = "${getString(R.string.app_name)} Document"

            // Get a print adapter instance
            val printAdapter = webView.createPrintDocumentAdapter(jobName)

            // Create a print job with name and adapter instance
            printManager.print(
                jobName,
                printAdapter,
                PrintAttributes.Builder().build()
            )
        }
    }
}
