package com.cezma.app.ui.mainActivity.how_to_open_shop

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
import com.cezma.app.utiles.Constants.howToOpenYourShopUrl
import com.cezma.app.utiles.ViewState
import com.cezma.app.utiles.changeLanguage
import com.cezma.app.utiles.toast
import kotlinx.android.synthetic.main.about_us_fragment.*
import kotlinx.android.synthetic.main.how_to_open_shop_fragment.*
import kotlinx.android.synthetic.main.how_to_open_shop_fragment.backImgv
import kotlinx.android.synthetic.main.how_to_open_shop_fragment.loading

class HowToOpenShopFragment : Fragment() {

    companion object {
        fun newInstance() = HowToOpenShopFragment()
    }

    private lateinit var viewModel: HowToOpenShopViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.how_to_open_shop_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HowToOpenShopViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onPageResponse(it) })

        activity?.changeLanguage()

        //secure the screen prevent Screen Shots
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        backImgv.setOnClickListener { findNavController().navigateUp() }


        howToOpenShopWv.setOnLongClickListener {
            // For final release of your app, comment the toast notification
            true
        }

        howToOpenShopWv.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                loading.visibility = View.GONE
            }
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
                howToOpenShopWv.settings.javaScriptEnabled = true
                howToOpenShopWv.loadDataWithBaseURL("", htmlData, "text/html", "UTF-8", "")

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
