package com.pixelbuilds.beinginmars

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navFragment = findNavController(R.id.fragmentContainerView1)
        bottomNav.setupWithNavController(navFragment)
        if (!networkDetails()) {
            val dialogBuilder = MaterialAlertDialogBuilder(this)
                .setTitle("Network Error")
                .setMessage("Seems Like You Didn't Connected To Any Of Network eg.,ETHERNET,CELLULAR NETWORK,WIFI.,\nConnect To The Available Network And Try Again To Access The Features!\nNote:- Application Will Be Closed Automatically When You Press 'Ok' As Network Was Not Detected.")
                .setPositiveButton("Ok") { dialog, which -> finishAndRemoveTask() }
                .setCancelable(false)
            dialogBuilder.show()
        } else {
            Toast.makeText(this, "Connected To The Network", Toast.LENGTH_SHORT).show()
        }

    }

     private fun networkDetails(): Boolean {
        val networkManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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

