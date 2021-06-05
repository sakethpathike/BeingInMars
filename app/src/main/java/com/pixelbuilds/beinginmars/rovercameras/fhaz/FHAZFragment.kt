@file:Suppress("MemberVisibilityCanBePrivate")

package com.pixelbuilds.beinginmars.rovercameras.fhaz

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.pixelbuilds.beinginmars.MainFragment
import com.pixelbuilds.beinginmars.R
import com.pixelbuilds.beinginmars.api.Constants
import kotlinx.android.synthetic.main.fragment_f_h_a_z.*
import kotlinx.android.synthetic.main.fragment_f_h_a_z.view.*

class FHAZFragment : Fragment() {
    private var fhazImages = arrayListOf<Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_f_h_a_z, container, false)
        imagesCollecting()
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        view.FHAZInfo.setOnClickListener {
            view.FHAZExplanation.textSize = 12.0F
            view.FHAZExplanation.text = getString(R.string.fhazNote)
        }
        view.FHAZInfo.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).setTitle("Information")
                .setMessage("Front Hazard Avoidance Camera's Data Is/Will Be Collected From NASA's API.\nIncluding:\n-Total Photos Clicked Data\n-Status Date Data\n-Launching Date\n-Landing Date Data\n-Earth Date Data\n-Image Of FHAZ\nAnd Major Data Which You Can See On Screen.")
                .setPositiveButton(
                    "Ok"
                ) { dialog, which -> }.setCancelable(false).show()
            view.FHAZExplanation.textSize = 12.0F
            view.FHAZExplanation.text = getString(R.string.click_on_image_to_know_more_about_it)
        }
        view.FHAZGlimpse.setOnClickListener {
            view.FHAZExplanation.textSize = 18.0F
            view.FHAZExplanation.text = getString(R.string.frontHazardText)
        }
        view.FHAZExplanation.setOnClickListener {
            view.FHAZExplanation.text = ""
        }
        return view
    }

    fun addImages(images: Int) {
        fhazImages.add(images)
    }

    private fun imagesCollecting() {
        val queue = Volley.newRequestQueue(requireContext())
        val json = JsonObjectRequest(Request.Method.GET, Constants.NASA_FHAZ_API, null, {
            val status = it.getJSONArray("photos").getJSONObject(0).getJSONObject("rover")
                .getString("status")
            val landingDate = it.getJSONArray("photos").getJSONObject(0).getJSONObject("rover")
                .getString("landing_date")
            val launchDate = it.getJSONArray("photos").getJSONObject(0).getJSONObject("rover")
                .getString("launch_date")
            val fhazGlimpse = it.getJSONArray("photos").getJSONObject(0).getString("img_src")
            val fhazEarthDate = it.getJSONArray("photos").getJSONObject(0).getString("earth_date")
            MainFragment().imageLoading(
                requireContext(),
                requireActivity().FHAZGlimpse,
                fhazGlimpse.toString(),
                requireActivity().progressBar
            )
            requireActivity().FHAZLaunchDate.text = launchDate.toString()
            requireActivity().FHAZLandingDate.text = landingDate.toString()
            requireActivity().FHAZEarthDate.text = fhazEarthDate.toString()
            requireActivity().FHAZStatus.text = status.toString()
        }, {
            Snackbar.make(
                requireView(),
                "Something Went Wrong While Fetching The Data...",
                Snackbar.LENGTH_LONG
            ).setBackgroundTint(
                Color.parseColor("#C13C3C")
            ).setTextColor(Color.parseColor("#FFFFFF")).show()
        })
        queue.add(json)

        for (item in 0..55) {
            addImages(R.drawable.rover)
        }
    }
}

