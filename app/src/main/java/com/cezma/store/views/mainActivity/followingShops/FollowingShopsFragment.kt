package com.cezma.store.views.mainActivity.followingShops

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.cezma.store.R
import kotlinx.android.synthetic.main.following_shops_fragment.*

class FollowingShopsFragment : Fragment() {

    companion object {
        fun newInstance() = FollowingShopsFragment()
    }

    private lateinit var viewModel: FollowingShopsViewModel
    private val adapterFollowingShopsFragment = AdapterFollowingShops()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.following_shops_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FollowingShopsViewModel::class.java)

        val followingShops = ArrayList<String>()
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")
        followingShops.add("a")

        adapterFollowingShopsFragment.replaceData(followingShops)

        followingShopsRv.adapter = adapterFollowingShopsFragment
        followingShopsRv.setHasFixedSize(true)


        followingShopBackImgv.setOnClickListener {
            findNavController().navigateUp()
        }

        followingShopTv.setOnClickListener {
            findNavController().navigate(R.id.action_followingShopsFragment_to_shopDetailsFragment)
        }
    }

}
