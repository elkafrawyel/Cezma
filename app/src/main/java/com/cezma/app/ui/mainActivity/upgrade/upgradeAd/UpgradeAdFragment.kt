package com.cezma.app.ui.mainActivity.upgrade.upgradeAd

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.cezma.app.R
import com.cezma.app.ui.mainActivity.adDetails.AdDetailsFragmentArgs
import com.cezma.app.utiles.ViewState
import com.cezma.app.utiles.changeLanguage
import com.cezma.app.utiles.toast
import kotlinx.android.synthetic.main.upgrade_account_fragment.*

class UpgradeAdFragment : Fragment() {

    companion object {
        fun newInstance() = UpgradeAdFragment()
    }

    private lateinit var viewModel: UpgradeAdViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.upgrade_ad_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UpgradeAdViewModel::class.java)

        viewModel.uiState.observe(this, Observer { onPageResponse(it) })

        arguments?.let {
            val adId = UpgradeAdFragmentArgs.fromBundle(it).adId
            viewModel.upgradeAd(adId)
        }

        upgradeAccountBackImgv.setOnClickListener {
            findNavController().navigateUp()
        }
        activity?.changeLanguage()

        //secure the screen prevent Screen Shots
//        requireActivity().window.setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE)


        webView.setOnLongClickListener {
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
                val url = viewModel.page
                webView.settings.javaScriptEnabled = true
                webView.loadUrl(url)

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
