package com.cezma.app.ui.mainActivity.adDetails

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide

import com.cezma.app.R
import com.cezma.app.data.model.AdImagesToSliderModel
import com.cezma.app.utiles.*
import kotlinx.android.synthetic.main.ad_details_fragment.*

class AdDetailsFragment : Fragment(), (Int) -> Unit {


    companion object {
        fun newInstance() = AdDetailsFragment()
    }

    private lateinit var viewModel: AdDetailsViewModel
    private val imageSliderAdapter = AdImageSliderAdapter(this)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.ad_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AdDetailsViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onAdResponse(it) })
        viewModel.uiStateFav.observe(this, Observer { onFavouriteActionResponse(it) })


        if (viewModel.ad == null) {
            arguments?.let {
                val adId = AdDetailsFragmentArgs.fromBundle(it).adId
                viewModel.adId = adId
                viewModel.getAd()
            }
        }

        backImgv.setOnClickListener { findNavController().navigateUp() }

        productPlayVideoImgv.setOnClickListener {
            //  val url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
            openYoutube(viewModel.ad?.youtube!!)
        }

        messageFL.setOnClickListener {
            val action =
                AdDetailsFragmentDirections.actionAdDetailsFragmentToChatRoomFragment("test")
            findNavController().navigate(action)
        }

        adDetailsFavImgv.setOnClickListener {
            makeAdFavourite()
        }

    }

    private fun onFavouriteActionResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {

                loading.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE
                    if (viewModel.isFavouriteAd) {
                        adDetailsFavImgv.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_border_white_24dp))
                        viewModel.isFavouriteAd = false
                        activity?.toast(resources.getString(R.string.notFav))
                    } else {
                        //add Fav
                        adDetailsFavImgv.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_red_24dp))
                        viewModel.isFavouriteAd = true
                        activity?.toast(resources.getString(R.string.fav))
                    }
            }
            ViewState.NoConnection -> {
                loading.visibility = View.GONE
                activity?.snackBarWithAction(
                    getString(R.string.noConnection),
                    getString(R.string.retry),
                    rootView
                ) {
                    viewModel.refresh()
                }
            }
            ViewState.Empty -> {

            }
            is ViewState.Error -> {
                loading.visibility = View.GONE
                activity?.snackBar(it.message, rootView)
            }
        }
    }

    private fun makeAdFavourite() {
        if (Injector.getPreferenceHelper().isLoggedIn) {
            viewModel.favouriteAction()
        } else {
            activity?.snackBarWithAction(
                getString(R.string.you_must_login),
                getString(R.string.login),
                rootView
            ) {
                findNavController().navigate(R.id.action_adDetailsFragment_to_loginFragment)

            }
        }
    }

    private fun openYoutube(youtubeLink: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(youtubeLink)
        context!!.startActivity(intent)
    }

    private fun onAdResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                dataCl.visibility = View.GONE

                loading.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                dataCl.visibility = View.VISIBLE
                loading.visibility = View.GONE
                setAdData()
            }
            ViewState.NoConnection -> {
                dataCl.visibility = View.GONE

                loading.visibility = View.GONE
                activity?.snackBarWithAction(
                    getString(R.string.noConnection),
                    getString(R.string.retry),
                    rootView
                ) {
                    viewModel.refresh()
                }
            }
            ViewState.Empty -> {

            }
            is ViewState.Error -> {
                dataCl.visibility = View.GONE

                loading.visibility = View.GONE
                activity?.snackBar(it.message, rootView)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setAdData() {

        val ad = viewModel.ad
        if (ad != null) {
            adTitleTv.text = ad.title
            adTitle.text = ad.title
            adPriceTv.text = "${ad.price} ${ad.currency}"
            Glide.with(context!!).load(ad.avatar).into(adUserImage)
            adUserName.text = ad.username
            adCategoryTv.text = ad.categoryName
            adSubCategoryTv.text = ad.subCategoryName
            adDescTv.text = ad.description

            // two lines here to make ImagesAdapter Show Us dots
            imageSliderAdapter.submitList(ad.photos)
            bannerSliderVp.adapter = imageSliderAdapter

            if (ad.youtube == null) {
                productPlayVideoImgv.visibility = View.INVISIBLE
            } else {
                productPlayVideoImgv.visibility = View.VISIBLE
            }

            //  Attributes
            val attributesAdapter = AttributesAdapter()
            attributesAdapter.replaceData(ad.attributeModels)
            attributesRv.setHasFixedSize(true)
            attributesRv.adapter = attributesAdapter


            if (ad.youtube != null) {
                productPlayVideoImgv.visibility = View.VISIBLE
            } else {
                productPlayVideoImgv.visibility = View.GONE
            }

            if (ad.fav == 1) {
                viewModel.isFavouriteAd = true
                adDetailsFavImgv.setImageDrawable(context!!.getDrawable(R.drawable.ic_favorite_red_24dp))
            } else {
                viewModel.isFavouriteAd = false
                adDetailsFavImgv.setImageDrawable(context!!.getDrawable(R.drawable.ic_favorite_border_white_24dp))
            }
        }
    }

    override fun invoke(position: Int) {
        val images = viewModel.ad!!.photos
        if (images.isNotEmpty()) {
            val action =
                AdDetailsFragmentDirections.actionAdDetailsFragmentToFullScreenSliderFragment(
                    position,
                    AdImagesToSliderModel(images)
                )
            findNavController().navigate(action)
        }
    }
}
