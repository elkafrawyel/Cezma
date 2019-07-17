package com.cezma.store.views.mainActivity.productDetails

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController

import com.cezma.store.R
import com.cezma.store.utiles.toast
import com.cezma.store.views.mainActivity.home.sub_home_fragment.HomeImageSliderAdapter
import com.cezma.store.views.playerActivity.PlayerActivity
import kotlinx.android.synthetic.main.product_details_fragment.*
import java.text.FieldPosition

class ProductDetailsFragment : Fragment(), (Int) -> Unit {


    companion object {
        fun newInstance() = ProductDetailsFragment()
    }

    private lateinit var viewModel: ProductDetailsViewModel
    private val imageSliderAdapter = ProductImageSliderAdapter(this)

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

        backImgv.setOnClickListener { findNavController().navigateUp() }

        productPlayVideoImgv.setOnClickListener {
            val url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
            PlayerActivity.start(context!!, url)
        }

        messageFL.setOnClickListener {
            val action = ProductDetailsFragmentDirections.actionProductDetailsFragmentToChatRoomFragment("test")
            findNavController().navigate(action)
        }
    }

    override fun invoke(position: Int) {
        //has to be with parameter
        activity?.toast("$position")
        findNavController().navigate(R.id.fullScreenSliderFragment)
    }


}
