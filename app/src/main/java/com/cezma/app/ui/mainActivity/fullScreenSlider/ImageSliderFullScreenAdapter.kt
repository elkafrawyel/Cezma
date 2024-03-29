package com.cezma.app.ui.mainActivity.fullScreenSlider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.cezma.app.R

class ImageSliderFullScreenAdapter( ) : PagerAdapter() {

    private val images = ArrayList<String>()

    override fun isViewFromObject(view: View, imgv: Any): Boolean {
        return view == imgv as ImageView
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = LayoutInflater.from(container.context)
            .inflate(R.layout.image_slider_full_screen_item, container, false) as ImageView

        container.addView(imageView)

        Glide.with(imageView)
            .load(images[position])
            .into(imageView)

        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    fun submitList(imagesList: List<String>) {
        images.clear()
        images.addAll(imagesList)
        notifyDataSetChanged()
    }

}