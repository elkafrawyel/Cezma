package com.cezma.app.ui.mainActivity.adDetails

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide

import com.cezma.app.R
import com.cezma.app.data.model.AdImagesToSliderModel
import com.cezma.app.data.model.AdOfferBody
import com.cezma.app.data.model.WriteCommentBody
import com.cezma.app.utiles.*
import com.koraextra.app.utily.observeEvent
import kotlinx.android.synthetic.main.ad_details_fragment.*

class AdDetailsFragment : Fragment() {

    private lateinit var viewModel: AdDetailsViewModel

    private val imageSliderAdapter = AdImageSliderAdapter { position ->
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
        viewModel.uiStateActions.observeEvent(this) { onActionsResponse(it) }

        if (viewModel.ad == null) {
            arguments?.let {
                val adId = AdDetailsFragmentArgs.fromBundle(it).adId
                viewModel.adId = adId
                viewModel.getAd()
            }
        }

        backImgv.setOnClickListener { findNavController().navigateUp() }

        productPlayVideoImgv.setOnClickListener {
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

        adDetailsReportImgv.setOnClickListener {
            reportAd()
        }

        adOfferMbtn.setOnClickListener {
            offerAd()
        }

        adCommentMbtn.setOnClickListener {
            if (viewModel.ad?.hasStore == 1) {
                openWriteCommentFragment()
            } else {
                openWriteCommentDialog()
            }
        }

        AdCommentsCount.setOnClickListener {
            val action =
                AdDetailsFragmentDirections.actionAdDetailsFragmentToAdCommentsFragment(viewModel.adId)
            findNavController().navigate(action)
        }
    }

    private fun openWriteCommentDialog() {
        val input = EditText(context)
        input.maxLines = 1
        input.inputType = InputType.TYPE_CLASS_TEXT

        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        input.layoutParams = lp

        val dialog = AlertDialog.Builder(context!!)
            .setView(input)
            .setTitle(resources.getString(R.string.app_name))
            .setMessage(resources.getString(R.string.writeComment))
            .setPositiveButton(android.R.string.ok, null)
            .setNegativeButton(android.R.string.cancel, null)
            .create()

        dialog.setOnShowListener { dialog1 ->

            val okBtn = (dialog1 as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            okBtn.setOnClickListener {
                val comment = input.text.toString()

                if (!TextUtils.isEmpty(comment)) {

                    viewModel.writeCommentBody = WriteCommentBody(viewModel.adId, comment, 0)
                    viewModel.setAction(AdActions.COMMENT)

                    dialog.cancel()

                } else {
                    input.error = resources.getString(R.string.emptyField)
                }
            }

            val cancel = dialog1.getButton(AlertDialog.BUTTON_NEGATIVE)
            cancel.setOnClickListener {
                dialog1.cancel()
            }
        }

        dialog.show()
    }

    private fun openWriteCommentFragment() {
        val action =
            AdDetailsFragmentDirections.actionAdDetailsFragmentToWriteCommentFragment(viewModel.adId)
        findNavController().navigate(action)
    }

    private fun onActionsResponse(it: ViewState) {
        when (it) {
            ViewState.Loading -> {
                loading.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE
                dataCl.visibility = View.VISIBLE
                onSuccess()
            }
            ViewState.NoConnection -> {
                loading.visibility = View.GONE
                dataCl.visibility = View.VISIBLE
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

    private fun onAdResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                loading.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE
                dataCl.visibility = View.VISIBLE
                setAdData()
            }
            ViewState.NoConnection -> {
                loading.visibility = View.GONE
                dataCl.visibility = View.VISIBLE
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

    private fun onSuccess() {
        when (viewModel.lastAction) {
            AdActions.Fav -> {
                if (viewModel.isFavouriteAd) {
                    adDetailsFavImgv.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_border_white_24dp))
                    viewModel.isFavouriteAd = false
                    viewModel.ad!!.fav = 0
                } else {
                    //add Fav
                    adDetailsFavImgv.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_red_24dp))
                    viewModel.isFavouriteAd = true
                    viewModel.ad!!.fav = 1
                }
                activity?.toast(viewModel.favMessage)
            }
            AdActions.Report -> {
                activity?.toast(viewModel.reportMessage)
            }
            AdActions.Offer -> {
                activity?.toast(viewModel.offerMessage)
            }
            AdActions.COMMENT -> {
                activity?.toast(viewModel.commentMessage)
            }

            null -> {

            }
        }
    }

    private fun offerAd() {
        if (Injector.getPreferenceHelper().isLoggedIn) {
            openAdOfferDialog()
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

    private fun openAdOfferDialog() {
        val input = EditText(context)
        input.maxLines = 1
        input.setLines(1)
        input.inputType = InputType.TYPE_CLASS_NUMBER

        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        input.layoutParams = lp

        val dialog = AlertDialog.Builder(context!!)
            .setView(input)
            .setTitle(resources.getString(R.string.app_name))
            .setMessage(resources.getString(R.string.adOfferMessage))
            .setPositiveButton(android.R.string.ok, null)
            .setNegativeButton(android.R.string.cancel, null)
            .create()

        dialog.setOnShowListener { dialog1 ->

            val okBtn = (dialog1 as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            okBtn.setOnClickListener {
                val offerPrice = input.text.toString()

                if (!TextUtils.isEmpty(offerPrice)) {
                    viewModel.adOfferBody = AdOfferBody(viewModel.adId, offerPrice)
                    viewModel.setAction(AdActions.Offer)
                    dialog.cancel()
                } else {
                    input.error = resources.getString(R.string.emptyField)
                }
            }

            val cancel = dialog1.getButton(AlertDialog.BUTTON_NEGATIVE)
            cancel.setOnClickListener {
                dialog1.cancel()
            }
        }

        dialog.show()
    }

    private fun reportAd() {
        if (Injector.getPreferenceHelper().isLoggedIn) {
            activity?.showMessageInDialog(getString(R.string.sureToReportAd), {
                viewModel.setAction(AdActions.Report)
            }, {
                // No Action Just Dismiss
            })

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

    private fun makeAdFavourite() {
        if (Injector.getPreferenceHelper().isLoggedIn) {
            viewModel.setAction(AdActions.Fav)
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

//            if (ad.isVerified){
//                adUserVerified.visibility = View.VISIBLE
//            }else{
//                adUserVerified.visibility = View.GONE
//            }

            if (ad.negotiable == 1) {
                adOfferMbtn.visibility = View.VISIBLE
            } else {
                adOfferMbtn.visibility = View.GONE
            }

            AdCommentsCount.text = getString(R.string.comments) + " (" + ad.commentsCount + ")"
        }
    }

}
