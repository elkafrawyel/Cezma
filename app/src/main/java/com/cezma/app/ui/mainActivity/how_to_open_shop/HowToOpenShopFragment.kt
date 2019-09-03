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
import androidx.navigation.fragment.findNavController

import com.cezma.app.R
import com.cezma.app.utiles.Constants.howToOpenYourShopUrl
import kotlinx.android.synthetic.main.how_to_open_shop_fragment.*

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
        //secure the screen prevent Screen Shots
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        );

        backImgv.setOnClickListener { findNavController().navigateUp() }

        howToOpenShopWv.loadUrl(howToOpenYourShopUrl)

        howToOpenShopWv.setOnLongClickListener(View.OnLongClickListener {
            // For final release of your app, comment the toast notification
            true
        })

        howToOpenShopWv.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                loading.visibility = View.GONE
            }
        }
    }

}
