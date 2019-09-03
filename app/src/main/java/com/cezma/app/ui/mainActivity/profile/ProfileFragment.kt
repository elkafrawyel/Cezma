package com.cezma.app.ui.mainActivity.profile

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.cezma.app.R
import com.cezma.app.ui.adapters.AdapterAds
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private val productAdapter = AdapterAds()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        val products = ArrayList<String>()
        products.add("A")
        products.add("A")
        products.add("A")
        products.add("A")
        products.add("A")
        products.add("A")
        products.add("A")
        products.add("A")
        products.add("A")
        products.add("A")
        products.add("A")
        products.add("A")
        products.add("A")
        products.add("A")
        products.add("A")
        products.add("A")
        products.add("A")
        products.add("A")
        adsRv.setHasFixedSize(true)
        adsRv.adapter = productAdapter


        profileEditImgv.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        profileBackImgv.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}
