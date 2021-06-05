package com.pixelbuilds.beinginmars

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import com.pixelbuilds.beinginmars.api.Constants
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*

@Suppress("MemberVisibilityCanBePrivate")
class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        view.NAVCAM.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.mainFragment_to_NAVCAMFragment)
        }
        view.PANCAM.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.mainFragment_to_PANCAMFragment)
        }
        view.MAST.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.mainFragment_to_mastFragment)
        }
        view.CHEMCAM.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.mainFragment_to_CHEMCAMFragment)
        }
        view.FHAZ.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.mainFragment_to_FHAZFragment)
        }
        view.RHAZ.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.mainFragment_to_RHAZFragment)
        }
        view.roverCamerasKnowMore.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).setTitle("Redirect")
                .setMessage("You Will Redirected To Another Screen Where You Can Know All About Rover's Camera(s) In-Depth.\nNote:- By Clicking On 'Ok' You Will Be Redirected To Another Page.")
                .setPositiveButton(
                    "Ok"
                ) { dialog, which ->
                    Navigation.findNavController(view).navigate(R.id.mainFragment_to_roverWebView)
                }.setNegativeButton(
                    "Cancel"
                ) { dialog, which -> }.setCancelable(false).show()
        }
        view.astronomyImage.setOnClickListener {
            view.astronomyImageExplanation.textSize = 18.0F
            jsonText(
                requireContext(),
                Constants.NASA_ASTRONOMY_API,
                "explanation",
                view.astronomyImageExplanation
            )
        }
        view.astronomyImageInfo.setOnClickListener {
            Snackbar.make(
                it,
                "Astronomy Image Of The Day's Data Is/Will Be Collected From NASA's API",
                Snackbar.LENGTH_LONG
            ).setBackgroundTint(
                Color.parseColor("#C13C3C")
            ).setTextColor(Color.parseColor("#FFFFFF")).show()
            view.astronomyImageExplanation.textSize = 12.0F
            view.astronomyImageExplanation.text =
                getString(R.string.click_on_image_to_know_more_about_it)
        }
        view.totalCameras.setOnClickListener {
            val btSheetDialog = BottomSheetDialog(requireContext())
            btSheetDialog.setContentView(R.layout.fragment_item_list_dialog_list_dialog)
            btSheetDialog.show()
        }
        view.totalPhotosInfo.setOnClickListener {
            Snackbar.make(
                it,
                "Total Image(s) Captured Data Is/Will Be Collected From NASA's API",
                Snackbar.LENGTH_LONG
            ).setBackgroundTint(
                Color.parseColor("#C13C3C")
            ).setTextColor(Color.parseColor("#FFFFFF")).show()
        }
        view.astronomyImageExplanation.setOnClickListener {
            astronomyImageExplanation.text = ""
        }
        jsonRequest(
            requireContext(),
            Constants.NASA_ASTRONOMY_API,
            view.astronomyImageTitle,
            view.astronomyImageDate,
            view.astronomyImage,
            "hdurl",
            "title",
            "date",
            view.progressBar
        )
        return view
    }

    fun jsonRequest(
        context: Context,
        apiURL: String,
        titleTextView: MaterialTextView,
        dateTextView: MaterialTextView,
        astronomyImageView: ImageView,
        requestImage: String,
        requestTitle: String,
        requestDate: String,
        progressBar: ProgressBar
    ) {
        val queue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, apiURL, null, { response ->
            val astronomyTitle = response!!.getString(requestTitle)
            titleTextView.text = astronomyTitle.toString()
            val astronomyDate = response.getString(requestDate)
            dateTextView.text = astronomyDate.toString()
            val astronomyImage = response.getString(requestImage)
            context.let {
                Glide.with(this)
                    .asDrawable()
                    .load(astronomyImage)
                    .listener(object : RequestListener<Drawable> {

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.VISIBLE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }
                    }).into(astronomyImageView)
            }
        }, {
            titleTextView.text = getString(R.string.error)
            dateTextView.text = getString(R.string.error)
        })
        queue.add(jsonObjectRequest)
    }

    fun jsonText(
        context: Context,
        apiKey: String,
        requestTitle: String,
        titleView: MaterialTextView
    ) {
        val query = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, apiKey, null, { response ->
            val titleRequest = response.getString(requestTitle)
            titleView.text = titleRequest.toString()
        }, {
            titleView.text = getString(R.string.error)
        })
        query.add(jsonObjectRequest)
    }

    fun imageLoading(
        context: Context,
        imageView: ImageView,
        imageURL: String,
        progressBar: ProgressBar
    ) {
        context.let {
            Glide.with(context)
                .asDrawable()
                .load(imageURL)
                .listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        Toast.makeText(context, "Something Went Wrong...", Toast.LENGTH_SHORT)
                            .show()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }
                }).into(imageView)
        }
    }
}