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
import kotlinx.android.synthetic.main.fragment_c_h_e_m_c_a_m.progressBar
import kotlinx.android.synthetic.main.fragment_n_a_v_c_a_m.*
import kotlinx.android.synthetic.main.fragment_n_a_v_c_a_m.view.*

class NAVCAMFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_n_a_v_c_a_m, container, false)
        imagesCollecting()
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        view.NAVCAMInfo.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).setTitle("Information")
                .setMessage("NAVCAM Camera's Data Is/Will Be Collected From NASA's API.\nIncluding:\n-Total Photos Clicked Data\n-Status Date Data\n-Launching Date\n-Landing Date Data\n-Earth Date Data\n-Image Of NAVCAM\nAnd Major Data Which You Can See On Screen.")
                .setPositiveButton(
                    "Ok"
                ) { dialog, which -> }.setCancelable(false).show()
        }
        view.NAVCAMExplanation.setOnClickListener {
            view.NAVCAMExplanation.text = ""
        }
        return view
    }


    private fun imagesCollecting() {
        val queue = Volley.newRequestQueue(requireContext())
        val json = JsonObjectRequest(Request.Method.GET, Constants.NASA_NAVCAM_API, null, {
            val status = it.getJSONArray("photos").getJSONObject(0).getJSONObject("rover")
                .getString("status")
            val landingDate = it.getJSONArray("photos").getJSONObject(0).getJSONObject("rover")
                .getString("landing_date")
            val launchDate = it.getJSONArray("photos").getJSONObject(0).getJSONObject("rover")
                .getString("launch_date")
            val navcamGlimpse = it.getJSONArray("photos").getJSONObject(0).getString("img_src")
            val navcamEarthDate = it.getJSONArray("photos").getJSONObject(0).getString("earth_date")
            val navcamTitle = it.getJSONArray("photos").getJSONObject(0).getJSONObject("camera")
                .getString("full_name")
            MainFragment().imageLoading(
                requireContext(),
                requireActivity().NAVCAMGlimpse,
                navcamGlimpse.toString(),
                requireActivity().progressBar
            )
            requireActivity().NAVCAMLaunchDate.text = launchDate.toString()
            requireActivity().NAVCAMLandingDate.text = landingDate.toString()
            requireActivity().NAVCAMEarthDate.text = navcamEarthDate.toString()
            requireActivity().NAVCAMStatus.text = status.toString()
            requireActivity().NAVCAMTitle.text = navcamTitle.toString()
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