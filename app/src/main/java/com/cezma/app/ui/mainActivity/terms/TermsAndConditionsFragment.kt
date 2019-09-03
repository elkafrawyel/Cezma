package com.cezma.app.ui.mainActivity.terms

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

import com.cezma.app.R
import com.cezma.app.utiles.Constants.TermsUrl
import kotlinx.android.synthetic.main.terms_and_conditions_fragment.*

class TermsAndConditionsFragment : Fragment() {

    companion object {
        fun newInstance() = TermsAndConditionsFragment()
    }

    private lateinit var viewModel: TremsAndConditionsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.terms_and_conditions_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TremsAndConditionsViewModel::class.java)


        //secure the screen prevent Screen Shots
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE);

        backImgv.setOnClickListener { findNavController().navigateUp() }

        termsWv.loadUrl(TermsUrl)

        termsWv.setOnLongClickListener {
            // For final release of your app, comment the toast notification
            true
        }

        termsWv.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                loading.visibility = View.GONE
            }
        }
    }

}
