package com.pixelbuilds.beinginmars.rovercameras.rhaz

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
import kotlinx.android.synthetic.main.fragment_f_h_a_z.progressBar
import kotlinx.android.synthetic.main.fragment_r_h_a_z.*
import kotlinx.android.synthetic.main.fragment_r_h_a_z.view.*

class RHAZFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_r_h_a_z, container, false)
        imagesCollecting()
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        view.RHAZInfo.setOnClickListener {
            view.RHAZExplanation.textSize = 12.0F
            view.RHAZExplanation.text = getString(R.string.rhazNote)
        }
        view.RHAZInfo.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).setTitle("Information")
                .setMessage("Rear Hazard Avoidance Camera's Data Is/Will Be Collected From NASA's API.\nIncluding:\n-Total Photos Clicked Data\n-Status Date Data\n-Launching Date\n-Landing Date Data\n-Earth Date Data\n-Image Of RHAZ\nAnd Major Data Which You Can See On Screen.")
                .setPositiveButton(
                    "Ok"
                ) { dialog, which -> }.setCancelable(false).show()
            view.RHAZExplanation.textSize = 12.0F
            view.RHAZExplanation.text = getString(R.string.click_on_image_to_know_more_about_it)
        }
        view.RHAZGlimpse.setOnClickListener {
            view.RHAZExplanation.textSize = 18.0F
            view.RHAZExplanation.text = getString(R.string.rearHazardText)
        }
        view.RHAZExplanation.setOnClickListener {
            view.RHAZExplanation.text = ""
        }
        return view
    }


    private fun imagesCollecting() {
        val queue = Volley.newRequestQueue(requireContext())
        val json = JsonObjectRequest(Request.Method.GET, Constants.NASA_RHAZ_API, null, {
            val status = it.getJSONArray("photos").getJSONObject(0).getJSONObject("rover")
                .getString("status")
            val landingDate = it.getJSONArray("photos").getJSONObject(0).getJSONObject("rover")
                .getString("landing_date")
            val launchDate = it.getJSONArray("photos").getJSONObject(0).getJSONObject("rover")
                .getString("launch_date")
            val rhazGlimpse = it.getJSONArray("photos").getJSONObject(0).getString("img_src")
            val rhazEarthDate = it.getJSONArray("photos").getJSONObject(0).getString("earth_date")
            MainFragment().imageLoading(
                requireContext(),
                requireActivity().RHAZGlimpse,
                rhazGlimpse.toString(),
                requireActivity().progressBar
            )
            requireActivity().RHAZLaunchDate.text = launchDate.toString()
            requireActivity().RHAZLandingDate.text = landingDate.toString()
            requireActivity().RHAZEarthDate.text = rhazEarthDate.toString()
            requireActivity().RHAZStatus.text = status.toString()
        }, {
            Snackbar.make(
                requireView(),
                "Something Went Wrong While Fetching Data...",
                Snackbar.LENGTH_LONG
            ).setBackgroundTint(
                Color.parseColor("#C13C3C")
            ).setTextColor(Color.parseColor("#FFFFFF")).show()
        })
        queue.add(json)
    }
}