package com.cezma.store.views.mainActivity.products

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.cezma.store.R
import com.cezma.store.views.mainActivity.home.sub_home_fragment.AdapterProducts
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.products_fragment.*

class ProductsFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {


    companion object {
        fun newInstance() = ProductsFragment()
    }

    private lateinit var viewModel: ProductsViewModel
    private val adapterProducts = AdapterProducts()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.products_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProductsViewModel::class.java)
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
        adapterProducts.onItemChildClickListener = this
        productsRv.adapter = adapterProducts
        productsRv.setHasFixedSize(true)


        productsBackImgv.setOnClickListener {
            findNavController().navigateUp()
        }

        searchTv.setOnClickListener {
            findNavController().navigate(R.id.action_productsFragment_to_searchFragment)
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        findNavController().navigate(R.id.action_productsFragment_to_productDetailsFragment)
    }

}
