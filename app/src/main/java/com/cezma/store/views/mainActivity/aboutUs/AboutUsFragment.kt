package com.cezma.store.views.mainActivity.aboutUs

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController

import com.cezma.store.R
import com.cezma.store.utiles.Constants.aboutUsUrl
import kotlinx.android.synthetic.main.about_us_fragment.*

class AboutUsFragment : Fragment() {

    companion object {
        fun newInstance() = AboutUsFragment()
    }

    private lateinit var viewModel: AboutUsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.about_us_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AboutUsViewModel::class.java)
        //secure the screen prevent Screen Shots
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE);

        backImgv.setOnClickListener { findNavController().navigateUp() }

        aboutUsWv.loadUrl(aboutUsUrl)

        aboutUsWv.setOnLongClickListener(View.OnLongClickListener {
            // For final release of your app, comment the toast notification
            true
        })

        aboutUsWv.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                loading.visibility = View.GONE
            }
        };
    }

}
