package com.cezma.store.views.mainActivity.followingShops

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.cezma.store.R

class FollowingShopsFragment : Fragment() {

    companion object {
        fun newInstance() = FollowingShopsFragment()
    }

    private lateinit var viewModel: FollowingShopsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.following_shops_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FollowingShopsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
