package com.pixelbuilds.beinginmars

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.navigation.Navigation
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FeedBackFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feed_back, container, false)
        val webView = view.findViewById<WebView>(R.id.feedBackView)
        if (!networkDetails()) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Network Error")
                .setMessage("Seems Like You Didn't Connected To Any Of Network eg.,ETHERNET,CELLULAR NETWORK,WIFI.,\nConnect To The Available Network And Try Again To Access The Features!\nNote:- You Will Be Redirected To The Explore Screen Automatically When You Press 'Ok' As Network Was Not Detected.")
                .setPositiveButton("Ok") { dialog, which ->
                    Navigation.findNavController(view)
                        .navigate(R.id.feedBackFragment_to_mainFragment)
                }
                .setCancelable(false)
                .show()
        } else {
            WebPageFragment().webView(
                webView,
                "https://docs.google.com/forms/d/e/1FAIpQLSefkuXL5tiyoR4MKIkanB4ZLmJcorBjJMzrf2vNuhxF2ADBwg/viewform"
            )
            val refresh = view.findViewById<SwipeRefreshLayout>(R.id.feedBackRefresh)
            refresh.setOnRefreshListener {
                WebPageFragment().webView(
                    webView,
                    "https://docs.google.com/forms/d/e/1FAIpQLSefkuXL5tiyoR4MKIkanB4ZLmJcorBjJMzrf2vNuhxF2ADBwg/viewform"
                )
                refresh.isRefreshing = false
            }

        }
        return view
    }
    private fun networkDetails(): Boolean {
        val networkManager = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val networkInfo = networkManager.activeNetwork
            val activeNetwork = networkManager.getNetworkCapabilities(networkInfo)
            return when {
                activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true || activeNetwork?.hasTransport(
                    NetworkCapabilities.TRANSPORT_ETHERNET
                ) == true || activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true || activeNetwork?.hasTransport(
                    NetworkCapabilities.TRANSPORT_BLUETOOTH
                ) == true -> true
                else -> false
            }
        } else {
            val networkInfo = networkManager.activeNetworkInfo
            return networkInfo?.isConnectedOrConnecting == true
        }
    }
}

