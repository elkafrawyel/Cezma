package com.cezma.store.views.mainActivity.productDetails

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController

import com.cezma.store.R
import com.cezma.store.views.mainActivity.home.sub_home_fragment.ImageSliderAdapter
import com.cezma.store.views.mainActivity.player.PlayerActivity
import kotlinx.android.synthetic.main.product_details_fragment.*

class ProductDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = ProductDetailsFragment()
    }

    private lateinit var viewModel: ProductDetailsViewModel
    private val imageSliderAdapter = ImageSliderAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.product_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProductDetailsViewModel::class.java)

        arguments?.let {
            val productId = ProductDetailsFragmentArgs.fromBundle(it).productId
            Toast.makeText(context, productId, Toast.LENGTH_LONG).show()
        }


        bannerSliderVp.adapter = imageSliderAdapter
        val images = ArrayList<String>()
        images.add("https://www.lenovo.com/medias/lenovo-laptop-thinkpad-x1-extreme-hero.png?context=bWFzdGVyfHJvb3R8NzkyMDF8aW1hZ2UvcG5nfGg2NC9oZDUvOTk4NjE1MTg0MTgyMi5wbmd8NDNhZGJlZTg2MjAwMmYyYTcyMDQ0NzIxNDIwODRiOWIxODliODY5ZWQ5NmZiOWQ0MTQ5MzM0YjIxMDJhZTFlMQ")
        images.add("https://www.lenovo.com/medias/lenovo-laptop-thinkpad-x1-extreme-hero.png?context=bWFzdGVyfHJvb3R8NzkyMDF8aW1hZ2UvcG5nfGg2NC9oZDUvOTk4NjE1MTg0MTgyMi5wbmd8NDNhZGJlZTg2MjAwMmYyYTcyMDQ0NzIxNDIwODRiOWIxODliODY5ZWQ5NmZiOWQ0MTQ5MzM0YjIxMDJhZTFlMQ")
        images.add("https://www.lenovo.com/medias/lenovo-laptop-thinkpad-x1-extreme-hero.png?context=bWFzdGVyfHJvb3R8NzkyMDF8aW1hZ2UvcG5nfGg2NC9oZDUvOTk4NjE1MTg0MTgyMi5wbmd8NDNhZGJlZTg2MjAwMmYyYTcyMDQ0NzIxNDIwODRiOWIxODliODY5ZWQ5NmZiOWQ0MTQ5MzM0YjIxMDJhZTFlMQ")
        images.add("https://www.lenovo.com/medias/lenovo-laptop-thinkpad-x1-extreme-hero.png?context=bWFzdGVyfHJvb3R8NzkyMDF8aW1hZ2UvcG5nfGg2NC9oZDUvOTk4NjE1MTg0MTgyMi5wbmd8NDNhZGJlZTg2MjAwMmYyYTcyMDQ0NzIxNDIwODRiOWIxODliODY5ZWQ5NmZiOWQ0MTQ5MzM0YjIxMDJhZTFlMQ")
        images.add("https://www.lenovo.com/medias/lenovo-laptop-thinkpad-x1-extreme-hero.png?context=bWFzdGVyfHJvb3R8NzkyMDF8aW1hZ2UvcG5nfGg2NC9oZDUvOTk4NjE1MTg0MTgyMi5wbmd8NDNhZGJlZTg2MjAwMmYyYTcyMDQ0NzIxNDIwODRiOWIxODliODY5ZWQ5NmZiOWQ0MTQ5MzM0YjIxMDJhZTFlMQ")
        imageSliderAdapter.submitList(images)

        productDetailsBackImgv.setOnClickListener { findNavController().navigateUp() }

//        productPlayVideoImgv.setOnClickListener { startActivity(Intent(context, PlayerActivity::class.java)) }

        messageFL.setOnClickListener {
            val action = ProductDetailsFragmentDirections.actionProductDetailsFragmentToChatRoomFragment("test")
            findNavController().navigate(action)
        }
    }

}
