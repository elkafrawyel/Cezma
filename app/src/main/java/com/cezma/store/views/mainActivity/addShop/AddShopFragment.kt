package com.cezma.store.views.mainActivity.addShop

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.cezma.store.R

class AddShopFragment : Fragment() {

    companion object {
        fun newInstance() = AddShopFragment()
    }

    private lateinit var viewModel: AddShopViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_shop_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddShopViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
