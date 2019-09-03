package com.cezma.app.ui.mainActivity.fullScreenSlider

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.cezma.app.R
import kotlinx.android.synthetic.main.fragment_full_screen_slider.*

class FullScreenSliderFragment : Fragment() {

    private val imageSliderAdapter = ImageSliderFullScreenAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_full_screen_slider, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val pos = FullScreenSliderFragmentArgs.fromBundle(it).selectedImagePos
            val adImages = FullScreenSliderFragmentArgs.fromBundle(it).adImages

            bannerSliderVp.adapter = imageSliderAdapter
            imageSliderAdapter.submitList(adImages.photos)
            bannerSliderVp.currentItem = pos
        }

    }
}
