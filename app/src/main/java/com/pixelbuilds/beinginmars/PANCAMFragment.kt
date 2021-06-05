package com.pixelbuilds.beinginmars

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
import com.pixelbuilds.beinginmars.api.Constants
import kotlinx.android.synthetic.main.fragment_n_a_v_c_a_m.progressBar
import kotlinx.android.synthetic.main.fragment_p_a_n_c_a_m.*
import kotlinx.android.synthetic.main.fragment_p_a_n_c_a_m.view.*

class PANCAMFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_p_a_n_c_a_m, container, false)
        imagesCollecting()
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        view.PANCAMInfo.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).setTitle("Information")
                .setMessage("PANCAM Camera's Data Is/Will Be Collected From NASA's API.\nIncluding:\n-Total Photos Clicked Data\n-Status Date Data\n-Launching Date\n-Landing Date Data\n-Earth Date Data\n-Image Of PANCAM\nAnd Major Data Which You Can See On Screen.")
                .setPositiveButton(
                    "Ok"
                ) { dialog, which -> }.setCancelable(false).show()
        }
        view.PANCAMExplanation.setOnClickListener {
            view.PANCAMExplanation.text = ""
        }
        return view
    }

    private fun imagesCollecting() {
        val queue = Volley.newRequestQueue(requireContext())
        val json = JsonObjectRequest(Request.Method.GET, Constants.NASA_PANCAM_API, null, {
            val status = it.getJSONArray("photos").getJSONObject(0).getJSONObject("rover")
                .getString("status")
            val landingDate = it.getJSONArray("photos").getJSONObject(0).getJSONObject("rover")
                .getString("landing_date")
            val launchDate = it.getJSONArray("photos").getJSONObject(0).getJSONObject("rover")
                .getString("launch_date")
            val pancamGlimpse = it.getJSONArray("photos").getJSONObject(0).getString("img_src")
            val pancamEarthDate = it.getJSONArray("photos").getJSONObject(0).getString("earth_date")
            val pancamTitle = it.getJSONArray("photos").getJSONObject(0).getJSONObject("camera")
                .getString("full_name")
            MainFragment().imageLoading(
                requireContext(),
                requireActivity().PANCAMGlimpse,
                pancamGlimpse.toString(),
                requireActivity().progressBar
            )
            requireActivity().PANCAMLaunchDate.text = launchDate.toString()
            requireActivity().PANCAMLandingDate.text = landingDate.toString()
            requireActivity().PANCAMEarthDate.text = pancamEarthDate.toString()
            requireActivity().PANCAMStatus.text = status.toString()
            requireActivity().PANCAMTitle.text = pancamTitle.toString()
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