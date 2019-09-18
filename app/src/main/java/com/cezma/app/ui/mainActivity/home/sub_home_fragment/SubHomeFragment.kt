package com.cezma.app.ui.mainActivity.home.sub_home_fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import com.cezma.app.R
import com.cezma.app.ui.adapters.AdapterAds
import com.cezma.app.ui.mainActivity.MainActivity
import com.cezma.app.ui.mainActivity.home.MainHomeFragmentDirections
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
    private val imageSliderAdapter = HomeImageSliderAdapter()
    private val adsAdapter = AdapterAds()
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
        images.add("https://www.lenovo.com/medias/lenovo-laptop-thinkpad-x1-extreme-hero.png?context=bWFzdGVyfHJvb3R8NzkyMDF8aW1hZ2UvcG5nfGg2NC9oZDUvOTk4NjE1MTg0MTgyMi5wbmd8NDNhZGJlZTg2MjAwMmYyYTcyMDQ0NzIxNDIwODRiOWIxODliODY5ZWQ5NmZiOWQ0MTQ5MzM0YjIxMDJhZTFlMQ")
        images.add("https://www.lenovo.com/medias/lenovo-laptop-thinkpad-x1-extreme-hero.png?context=bWFzdGVyfHJvb3R8NzkyMDF8aW1hZ2UvcG5nfGg2NC9oZDUvOTk4NjE1MTg0MTgyMi5wbmd8NDNhZGJlZTg2MjAwMmYyYTcyMDQ0NzIxNDIwODRiOWIxODliODY5ZWQ5NmZiOWQ0MTQ5MzM0YjIxMDJhZTFlMQ")
        images.add("https://www.lenovo.com/medias/lenovo-laptop-thinkpad-x1-extreme-hero.png?context=bWFzdGVyfHJvb3R8NzkyMDF8aW1hZ2UvcG5nfGg2NC9oZDUvOTk4NjE1MTg0MTgyMi5wbmd8NDNhZGJlZTg2MjAwMmYyYTcyMDQ0NzIxNDIwODRiOWIxODliODY5ZWQ5NmZiOWQ0MTQ5MzM0YjIxMDJhZTFlMQ")
        images.add("https://www.lenovo.com/medias/lenovo-laptop-thinkpad-x1-extreme-hero.png?context=bWFzdGVyfHJvb3R8NzkyMDF8aW1hZ2UvcG5nfGg2NC9oZDUvOTk4NjE1MTg0MTgyMi5wbmd8NDNhZGJlZTg2MjAwMmYyYTcyMDQ0NzIxNDIwODRiOWIxODliODY5ZWQ5NmZiOWQ0MTQ5MzM0YjIxMDJhZTFlMQ")
        images.add("https://www.lenovo.com/medias/lenovo-laptop-thinkpad-x1-extreme-hero.png?context=bWFzdGVyfHJvb3R8NzkyMDF8aW1hZ2UvcG5nfGg2NC9oZDUvOTk4NjE1MTg0MTgyMi5wbmd8NDNhZGJlZTg2MjAwMmYyYTcyMDQ0NzIxNDIwODRiOWIxODliODY5ZWQ5NmZiOWQ0MTQ5MzM0YjIxMDJhZTFlMQ")
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

        adsRv.setHasFixedSize(true)
        adsAdapter.setOnItemChildClickListener { adapter, view, position ->
            val action =
                MainHomeFragmentDirections.actionMainHomeFragmentToProductDetailsFragment("test")
            activity?.findNavController(R.id.fragment)!!.navigate(action)
        }
        adsRv.adapter = adsAdapter

        addProductFab.setOnClickListener {
            Navigation.findNavController(activity as MainActivity, R.id.fragment).navigate(R.id.action_mainHomeFragment_to_addProductFragment)
        }
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