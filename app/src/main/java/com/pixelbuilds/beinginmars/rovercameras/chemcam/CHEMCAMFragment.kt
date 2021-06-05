package com.pixelbuilds.beinginmars.rovercameras.chemcam

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.pixelbuilds.beinginmars.MainFragment
import com.pixelbuilds.beinginmars.R
import com.pixelbuilds.beinginmars.api.Constants
import kotlinx.android.synthetic.main.fragment_c_h_e_m_c_a_m.*
import kotlinx.android.synthetic.main.fragment_c_h_e_m_c_a_m.view.*
import kotlinx.android.synthetic.main.fragment_r_h_a_z.progressBar

class CHEMCAMFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_c_h_e_m_c_a_m, container, false)
        imagesCollecting()
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        view.CHEMCAMInfo.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).setTitle("Information")
                .setMessage("CHEMCAM Camera's Data Is/Will Be Collected From NASA's API.\nIncluding:\n-Total Photos Clicked Data\n-Status Date Data\n-Launching Date\n-Landing Date Data\n-Earth Date Data\n-Image Of CHEMCAM\nAnd Major Data Which You Can See On Screen.")
                .setPositiveButton(
                    "Ok"
                ) { dialog, which -> }.setCancelable(false).show()
        }
        view.CHEMCAMExplanation.setOnClickListener {
            view.CHEMCAMExplanation.text = ""
        }
        return view
    }


    private fun imagesCollecting() {
        val queue = Volley.newRequestQueue(requireContext())
        val json = JsonObjectRequest(Request.Method.GET, Constants.NASA_CHEMCAM_API, null, {
            val status = it.getJSONArray("photos").getJSONObject(0).getJSONObject("rover")
                .getString("status")
            val landingDate = it.getJSONArray("photos").getJSONObject(0).getJSONObject("rover")
                .getString("landing_date")
            val launchDate = it.getJSONArray("photos").getJSONObject(0).getJSONObject("rover")
                .getString("launch_date")
            val chemcamGlimpse = it.getJSONArray("photos").getJSONObject(0).getString("img_src")
            val chemcamEarthDate =
                it.getJSONArray("photos").getJSONObject(0).getString("earth_date")
            val chemcamTitle = it.getJSONArray("photos").getJSONObject(0).getJSONObject("camera")
                .getString("full_name")
            MainFragment().imageLoading(
                requireContext(),
                requireActivity().CHEMCAMGlimpse,
                chemcamGlimpse.toString(),
                requireActivity().progressBar
            )
            requireActivity().CHEMCAMLaunchDate.text = launchDate.toString()
            requireActivity().CHEMCAMLandingDate.text = landingDate.toString()
            requireActivity().CHEMCAMEarthDate.text = chemcamEarthDate.toString()
            requireActivity().CHEMCAMStatus.text = status.toString()
            requireActivity().CHEMCAMTitle.text = chemcamTitle.toString()
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
    }
}