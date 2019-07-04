package com.cezma.store.views.mainActivity.how_to_open_shop

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.cezma.store.R

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
        // TODO: Use the ViewModel
    }

}