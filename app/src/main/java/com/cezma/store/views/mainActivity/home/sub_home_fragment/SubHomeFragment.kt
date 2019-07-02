package com.cezma.store.views.mainActivity.home.sub_home_fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import com.cezma.store.R
import kotlinx.android.synthetic.main.sub_home_fragment.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timerTask

class SubHomeFragment : Fragment() {

    companion object {
        fun newInstance() = SubHomeFragment()
    }

    private lateinit var viewModel: SubHomeFragmentViewModel
    private var timer: Timer? = null
    private val imageSliderAdapter = ImageSliderAdapter()
    private val productAdapter = AdapterProducts()
    private val categoriesAdapter = AdapterCategories()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sub_home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SubHomeFragmentViewModel::class.java)
        bannerSliderVp.adapter = imageSliderAdapter
        val images = ArrayList<String>()
        images.add("https://public-v2links.adobecc.com/1968c231-767d-4049-7540-7fb9f5a1e33d/component?params=component_id:82183b58-1f89-4a23-b141-3f7274937b67&params=version:0&token=1562063168_da39a3ee_22367b3f1f36885d0bcd9caca7114ffa871a2fbc&api_key=CometServer1")
        images.add("https://public-v2links.adobecc.com/1968c231-767d-4049-7540-7fb9f5a1e33d/component?params=component_id:82183b58-1f89-4a23-b141-3f7274937b67&params=version:0&token=1562063168_da39a3ee_22367b3f1f36885d0bcd9caca7114ffa871a2fbc&api_key=CometServer1")
        images.add("https://public-v2links.adobecc.com/1968c231-767d-4049-7540-7fb9f5a1e33d/component?params=component_id:82183b58-1f89-4a23-b141-3f7274937b67&params=version:0&token=1562063168_da39a3ee_22367b3f1f36885d0bcd9caca7114ffa871a2fbc&api_key=CometServer1")
        images.add("https://public-v2links.adobecc.com/1968c231-767d-4049-7540-7fb9f5a1e33d/component?params=component_id:82183b58-1f89-4a23-b141-3f7274937b67&params=version:0&token=1562063168_da39a3ee_22367b3f1f36885d0bcd9caca7114ffa871a2fbc&api_key=CometServer1")
        imageSliderAdapter.submitList(images)

        val categories = ArrayList<String>()
        categories.add("A")
        categories.add("A")
        categories.add("A")
        categories.add("A")
        categories.add("A")
        categories.add("A")
        categories.add("A")
        categories.add("A")
        categories.add("A")
        categories.add("A")

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = HORIZONTAL
        categoriesRv.layoutManager = layoutManager
        categoriesRv.setHasFixedSize(true)
        categoriesAdapter.replaceData(categories)
        categoriesRv.adapter = categoriesAdapter

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
        productsRv.setHasFixedSize(true)
        productAdapter.replaceData(products)
        productsRv.adapter = productAdapter
    }

    override fun onResume() {
        super.onResume()
        timer = Timer()
        timer?.scheduleAtFixedRate(timerTask {
            requireActivity().runOnUiThread {
                if (bannerSliderVp != null) {
                    if (bannerSliderVp.currentItem < imageSliderAdapter.count - 1) {
                        bannerSliderVp.setCurrentItem(bannerSliderVp.currentItem + 1, true)
                    } else {
                        bannerSliderVp.setCurrentItem(0, true)
                    }
                }
            }
        }, 5000, 5000)
    }

    override fun onPause() {
        timer?.cancel()
        super.onPause()
    }

}
