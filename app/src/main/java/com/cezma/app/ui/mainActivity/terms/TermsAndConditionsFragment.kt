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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.cezma.app.R
import com.cezma.app.utiles.Constants.TermsUrl
import com.cezma.app.utiles.ViewState
import com.cezma.app.utiles.changeLanguage
import com.cezma.app.utiles.toast
import kotlinx.android.synthetic.main.privacy_policy_fragment.*
import kotlinx.android.synthetic.main.terms_and_conditions_fragment.*
import kotlinx.android.synthetic.main.terms_and_conditions_fragment.backImgv
import kotlinx.android.synthetic.main.terms_and_conditions_fragment.loading

class TermsAndConditionsFragment : Fragment() {

    companion object {
        fun newInstance() = TermsAndConditionsFragment()
    }

    private lateinit var viewModel: TermsAndConditionsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.terms_and_conditions_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TermsAndConditionsViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onPageResponse(it) })

        activity?.changeLanguage()

        //secure the screen prevent Screen Shots
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE);

        backImgv.setOnClickListener { findNavController().navigateUp() }

        termsWv.setOnLongClickListener {
            // For final release of your app, comment the toast notification
            true
        }

    }

    private fun onPageResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                loading.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE
                val htmlData = viewModel.page?.pageContent
                termsWv.settings.javaScriptEnabled = true
                termsWv.loadDataWithBaseURL("", htmlData, "text/html", "UTF-8", "")

            }
            is ViewState.Error -> {
                loading.visibility = View.GONE
            }
            ViewState.NoConnection -> {
                loading.visibility = View.GONE
                activity?.toast(
                    getString(R.string.noConnection)
                )
            }
            null -> {

            }
        }
    }
}
