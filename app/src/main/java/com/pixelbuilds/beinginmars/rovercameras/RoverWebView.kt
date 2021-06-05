package com.pixelbuilds.beinginmars.rovercameras

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pixelbuilds.beinginmars.R
import com.pixelbuilds.beinginmars.WebPageFragment

class RoverWebView : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rover_web_view, container, false)
        val webView = view.findViewById<WebView>(R.id.roverWebView1)
        WebPageFragment().webView(
            webView,
            "https://mars.nasa.gov/mars2020/spacecraft/rover/cameras/"
        )
        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.swipeDownToRefreshRoverCamera)
        refresh.setOnRefreshListener {
            WebPageFragment().webView(
                webView,
                "https://mars.nasa.gov/mars2020/spacecraft/rover/cameras/"
            )
            refresh.isRefreshing = false
        }
        return view
    }
}