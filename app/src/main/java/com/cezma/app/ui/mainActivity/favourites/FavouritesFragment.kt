package com.cezma.app.ui.mainActivity.favourites

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cezma.app.R
import com.cezma.app.data.model.FavoriteAd
import com.cezma.app.utiles.ViewState
import com.cezma.app.utiles.snackBar
import com.cezma.app.utiles.snackBarWithAction
import com.cezma.app.utiles.toast
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.favourites_fragment.*

class FavouritesFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {


    companion object {
        fun newInstance() = FavouritesFragment()
    }

    private lateinit var viewModel: FavouritesViewModel
    private val adapterAds = AdapterFavourites().also {
        it.onItemChildClickListener = this
    }

    private var favPosition: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favourites_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FavouritesViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onFavouriteAdsResponse(it) })
        viewModel.uiStateFav.observe(this, Observer { onFavouriteActionResponse(it) })

        adsRv.setHasFixedSize(true)
        adsRv.adapter = adapterAds

        favouritesBackImgv.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun onFavouriteAdsResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                adsRv.visibility = View.GONE
                loading.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                adsRv.visibility = View.VISIBLE
                loading.visibility = View.GONE
                if (viewModel.favouriteAds.isNotEmpty())
                    adapterAds.replaceData(viewModel.favouriteAds)
                else {
                    loading.visibility = View.GONE
                    adsRv.visibility = View.GONE
                    emptyView.visibility = View.VISIBLE
                    emptyView.text = resources.getString(R.string.emptyFavouriteList)
                }
            }
            ViewState.NoConnection -> {
                adsRv.visibility = View.GONE

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
                loading.visibility = View.GONE
                adsRv.visibility = View.GONE
                emptyView.visibility = View.VISIBLE
                emptyView.text = resources.getString(R.string.emptyFavouriteList)
            }

            is ViewState.Error -> {
                adsRv.visibility = View.GONE
                loading.visibility = View.GONE
                activity?.snackBar(it.message, rootView)
            }
        }
    }

    private fun onFavouriteActionResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                loading.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE
                if (favPosition != null) {
                    viewModel.favouriteAds.removeAt(favPosition!!)
                    adapterAds.data.removeAt(favPosition!!)
                    adapterAds.notifyItemRemoved(favPosition!!)
                    loading.visibility = View.GONE
                    adsRv.visibility = View.GONE
                    emptyView.visibility = View.VISIBLE
                    emptyView.text = resources.getString(R.string.emptyFavouriteList)
                }
            }
            ViewState.NoConnection -> {
                loading.visibility = View.GONE
                activity?.toast(getString(R.string.noConnection))
            }
            ViewState.Empty -> {

            }
            is ViewState.Error -> {
                loading.visibility = View.GONE
                activity?.snackBar(it.message, rootView)
            }
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val ad = adapter?.data as List<FavoriteAd>
        when (view?.id) {
            R.id.favouriteItemFavImg -> {
                favPosition = position
                removeAdFromFavorite(ad[position].adId)
            }

            R.id.adItem -> {
                openAdDetails(ad[position].adId)
            }

        }
    }

    private fun openAdDetails(adId: Long) {
        val action =
            FavouritesFragmentDirections.actionFavouritesFragmentToAdDetailsFragment(adId.toString())
        findNavController().navigate(action)
    }

    private fun removeAdFromFavorite(adId: Long) {
        viewModel.adId = adId.toString()
        viewModel.favouriteAction()
    }
}
