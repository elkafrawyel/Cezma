package com.cezma.app.ui.mainActivity.privacyPolicy

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
import com.cezma.app.utiles.Constants.privacyUrl
import kotlinx.android.synthetic.main.privacy_policy_fragment.*
import kotlinx.android.synthetic.main.privacy_policy_fragment.backImgv
import kotlinx.android.synthetic.main.privacy_policy_fragment.loading

class PrivacyPolicyFragment : Fragment() {

    companion object {
        fun newInstance() = PrivacyPolicyFragment()
    }

    private lateinit var viewModel: PrivacyPolicyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.privacy_policy_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PrivacyPolicyViewModel::class.java)
        //secure the screen prevent Screen Shots
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE);

        backImgv.setOnClickListener { findNavController().navigateUp() }

        privacyWv.loadUrl(privacyUrl)

        privacyWv.setOnLongClickListener(View.OnLongClickListener {
            // For final release of your app, comment the toast notification
            true
        })

        privacyWv.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                loading.visibility = View.GONE
            }
        };
    }

}
