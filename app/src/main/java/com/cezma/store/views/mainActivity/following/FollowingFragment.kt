package com.cezma.store.views.mainActivity.following

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.cezma.store.R
import kotlinx.android.synthetic.main.following_fragment.*

class FollowingFragment : Fragment() {

    companion object {
        fun newInstance() = FollowingFragment()
    }

    private lateinit var viewModel: FollowingViewModel
    private val adapterFollowing = AdapterFollowing()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.following_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FollowingViewModel::class.java)
        val following = ArrayList<String>()
        following.add("a")
        following.add("a")
        following.add("a")
        following.add("a")
        following.add("a")
        following.add("a")
        following.add("a")
        following.add("a")
        following.add("a")
        following.add("a")
        following.add("a")
        following.add("a")
        following.add("a")
        following.add("a")
        following.add("a")
        following.add("a")
        following.add("a")
        following.add("a")
        following.add("a")
        following.add("a")
        following.add("a")
        following.add("a")
        following.add("a")
        following.add("a")
        following.add("a")
        following.add("a")
        following.add("a")

        adapterFollowing.replaceData(following)

        followingRv.adapter = adapterFollowing
        followingRv.setHasFixedSize(true)

        followingBackImgv.setOnClickListener {
            findNavController().navigateUp()
        }

    }

}
