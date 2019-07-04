package com.cezma.store.views.mainActivity.shopDetails

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.cezma.store.R
import com.cezma.store.views.mainActivity.home.sub_home_fragment.AdapterProducts
import kotlinx.android.synthetic.main.shop_details_fragment.*

class ShopDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = ShopDetailsFragment()
    }

    private lateinit var viewModel: ShopDetailsViewModel
    private val adapterProducts = AdapterProducts()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shop_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ShopDetailsViewModel::class.java)

        val products = ArrayList<String>()
        products.add("a")
        products.add("a")
        products.add("a")
        products.add("a")
        products.add("a")
        products.add("a")
        products.add("a")
        products.add("a")
        products.add("a")
        products.add("a")
        products.add("a")
        products.add("a")
        products.add("a")
        products.add("a")
        products.add("a")
        products.add("a")
        products.add("a")
        products.add("a")
        products.add("a")
        products.add("a")
        products.add("a")

        adapterProducts.replaceData(products)
        productsRv.adapter = adapterProducts
        productsRv.setHasFixedSize(true)


//        shopDetailsBackImgv.setOnClickListener {
//            findNavController().navigateUp()
//        }
    }

}
