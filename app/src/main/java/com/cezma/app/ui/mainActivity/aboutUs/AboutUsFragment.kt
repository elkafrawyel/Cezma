package com.cezma.app.ui.mainActivity.aboutUs

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
import com.cezma.app.utiles.Constants.aboutUsUrl
import com.cezma.app.utiles.ViewState
import com.cezma.app.utiles.changeLanguage
import com.cezma.app.utiles.toast
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
        viewModel.uiState.observe(this, Observer { onPageResponse(it) })

        activity?.changeLanguage()

        //secure the screen prevent Screen Shots
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        backImgv.setOnClickListener { findNavController().navigateUp() }

        aboutUsWv.setOnLongClickListener {
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
                aboutUsWv.settings.javaScriptEnabled = true
                aboutUsWv.loadDataWithBaseURL("", htmlData, "text/html", "UTF-8", "")

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
