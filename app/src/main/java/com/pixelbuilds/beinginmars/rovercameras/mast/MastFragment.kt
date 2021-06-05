package com.pixelbuilds.beinginmars.rovercameras.mast

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
import kotlinx.android.synthetic.main.fragment_mast.*
import kotlinx.android.synthetic.main.fragment_mast.view.*
import kotlinx.android.synthetic.main.fragment_r_h_a_z.progressBar

class MastFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mast, container, false)
        imagesCollecting()
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        view.MASTInfo.setOnClickListener {
            view.MASTExplanation.textSize = 12.0F
            view.MASTExplanation.text = getString(R.string.mastNote)
        }
        view.MASTInfo.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).setTitle("Information")
                .setMessage("Mast Camera's Data Is/Will Be Collected From NASA's API.\nIncluding:\n-Total Photos Clicked Data\n-Status Date Data\n-Launching Date\n-Landing Date Data\n-Earth Date Data\n-Image Of MAST\nAnd Major Data Which You Can See On Screen.")
                .setPositiveButton(
                    "Ok"
                ) { dialog, which -> }.setCancelable(false).show()
            view.MASTExplanation.textSize = 12.0F
            view.MASTExplanation.text = getString(R.string.click_on_image_to_know_more_about_it)
        }
        view.MASTGlimpse.setOnClickListener {
            view.MASTExplanation.textSize = 18.0F
            view.MASTExplanation.text = getString(R.string.mastText)
        }
        view.MASTExplanation.setOnClickListener {
            view.MASTExplanation.text = ""
        }
        return view
    }


    private fun imagesCollecting() {
        val queue = Volley.newRequestQueue(requireContext())
        val json = JsonObjectRequest(Request.Method.GET, Constants.NASA_MAST_API, null, {
            val status = it.getJSONArray("photos").getJSONObject(0).getJSONObject("rover")
                .getString("status")
            val landingDate = it.getJSONArray("photos").getJSONObject(0).getJSONObject("rover")
                .getString("landing_date")
            val launchDate = it.getJSONArray("photos").getJSONObject(0).getJSONObject("rover")
                .getString("launch_date")
            val mastGlimpse = it.getJSONArray("photos").getJSONObject(0).getString("img_src")
            val mastEarthDate = it.getJSONArray("photos").getJSONObject(0).getString("earth_date")
            val mastTitle = it.getJSONArray("photos").getJSONObject(0).getJSONObject("camera")
                .getString("full_name")
            MainFragment().imageLoading(
                requireContext(),
                requireActivity().MASTGlimpse,
                mastGlimpse.toString(),
                requireActivity().progressBar
            )
            requireActivity().MASTLaunchDate.text = launchDate.toString()
            requireActivity().MASTLandingDate.text = landingDate.toString()
            requireActivity().MASTEarthDate.text = mastEarthDate.toString()
            requireActivity().MASTStatus.text = status.toString()
            requireActivity().MASTTitle.text = mastTitle.toString()
        }, {
            Snackbar.make(
                requireView(),
                "Something Went Wrong While Fetching Status...",
                Snackbar.LENGTH_LONG
            ).setBackgroundTint(
                Color.parseColor("#C13C3C")
            ).setTextColor(Color.parseColor("#FFFFFF")).show()
        })
        queue.add(json)
    }
}
    
