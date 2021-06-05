package com.pixelbuilds.beinginmars

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class WebPageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_web_page, container, false)
        val webView = view.findViewById<WebView>(R.id.webView)
        webView(webView, "https://mars.nasa.gov/")
        val swipeDownToRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipeDownToRefresh)
        swipeDownToRefresh.setOnRefreshListener {
            webView(webView, "https://mars.nasa.gov/")
            swipeDownToRefresh.isRefreshing = false
        }
        return view
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun webView(webView: WebView, webURL: String) {
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                url: String?
            ): Boolean {
                view?.loadUrl(url!!)
                return true
            }
        }
        webView.loadUrl(webURL)
    }
}