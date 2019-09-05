package com.cezma.app.ui.mainActivity.profile

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide

import com.cezma.app.R
import com.cezma.app.ui.adapters.AdapterAds
import com.cezma.app.utiles.ViewState
import com.cezma.app.utiles.snackBarWithAction
import com.cezma.app.utiles.toast
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private val adapterAds = AdapterAds()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onUserProfileResponse(it) })

        adsRv.setHasFixedSize(true)
        adsRv.adapter = adapterAds

        profileEditImgv.setOnClickListener {
            if (viewModel.userModel != null) {
                val action =
                    ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(
                    viewModel.userModel!!
                )
                findNavController().navigate(action)
            }
        }

        profileBackImgv.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun onUserProfileResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                loading.visibility = View.VISIBLE
                dataCl.visibility = View.GONE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE
                dataCl.visibility = View.VISIBLE
                setUserProfile()
            }
            ViewState.NoConnection -> {

                loading.visibility = View.GONE
                dataCl.visibility = View.VISIBLE
                activity?.snackBarWithAction(
                    getString(R.string.noConnection),
                    getString(R.string.retry),
                    rootView
                ) {
                    viewModel.refresh()
                }
            }
            is ViewState.Error -> {
                loading.visibility = View.GONE
                dataCl.visibility = View.VISIBLE
                activity?.toast(it.message)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUserProfile() {
        val user = viewModel.userModel
        if (user != null) {
            Glide.with(context!!).load(user.avatar).into(profileUserImage)
            profileUserName.text = user.username
        }

        if (viewModel.ads.isEmpty()){
            adsRv.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
            emptyView.text = resources.getString(R.string.emptyList)
        }else{
            adsRv.visibility = View.VISIBLE
            adapterAds.replaceData(viewModel.ads)
        }
    }

}
