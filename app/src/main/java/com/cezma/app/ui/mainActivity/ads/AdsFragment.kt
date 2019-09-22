package com.cezma.app.ui.mainActivity.ads

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.cezma.app.R
import com.cezma.app.data.model.Ad
import com.cezma.app.ui.adapters.AdapterAds
import com.cezma.app.utiles.CustomLoadMoreView
import com.cezma.app.utiles.ViewState
import com.cezma.app.utiles.snackBarWithAction
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.ads_fragment.*

class AdsFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = AdsFragment()
    }

    private lateinit var viewModel: AdsViewModel
    private val adapterAds = AdapterAds().also {
        it.onItemChildClickListener = this
        it.setEnableLoadMore(true)
        it.setOnLoadMoreListener({ viewModel.getAds(true) }, adsRv)
        it.setLoadMoreView(CustomLoadMoreView())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.ads_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AdsViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onAdsResponse(it) })

        if (viewModel.adsList.isEmpty()) {
            arguments?.let {
                val categoryNameApi = AdsFragmentArgs.fromBundle(it).categoryNameApi
                val subCategoryName = AdsFragmentArgs.fromBundle(it).subCategoryName
                val subCategoryNameApi = AdsFragmentArgs.fromBundle(it).subCategoryNameApi

                viewModel.categoryNameApi = categoryNameApi
                viewModel.subCategoryName = subCategoryName
                viewModel.subCategoryNameApi = subCategoryNameApi

                viewModel.getAds()
            }
        }

        subCategoryTitle.text = viewModel.subCategoryName

        adapterAds.setEnableLoadMore(true)
        adsRv.adapter = adapterAds
        adsRv.setHasFixedSize(true)

        productsBackImgv.setOnClickListener {
            findNavController().navigateUp()
        }

        searchImg.setOnClickListener {
            findNavController().navigate(R.id.action_productsFragment_to_searchFragment)
        }

    }

    private fun onAdsResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                onLoading()
            }
            ViewState.Success -> {
                onSuccess()
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
                onEmpty()
            }
            is ViewState.Error -> {
                onError(it.message)
            }
            ViewState.LastPage -> {
                emptyView.visibility = View.GONE
                loading.visibility = View.GONE
                try {
                    adapterAds.loadMoreEnd()
                } catch (e: Exception) {
                }
            }
            null -> {

            }
        }
    }

    private fun onLoading() {
        loading.visibility = View.VISIBLE
        emptyView.visibility = View.GONE
        adsRv.visibility = View.GONE
    }

    private fun onSuccess() {
        loading.visibility = View.GONE
        emptyView.visibility = View.GONE
        adsRv.visibility = View.VISIBLE
        adapterAds.addData(viewModel.adsList)
        adapterAds.loadMoreComplete()
    }

    private fun onError(message: String) {
        loading.visibility = View.GONE
        emptyView.visibility = View.GONE
        adsRv.visibility = View.GONE
        adapterAds.loadMoreFail()
        activity?.snackBarWithAction(message, getString(R.string.retry), rootView) {
            viewModel.refresh()
        }
    }

    private fun onEmpty() {
        loading.visibility = View.GONE
        emptyView.visibility = View.VISIBLE
        emptyView.text = getString(R.string.emptyList)
        adsRv.visibility = View.GONE
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.adItem -> {
                val adId = (adapter?.data as List<Ad>)[position].adId
                val action =
                    AdsFragmentDirections.actionAdsFragmentToAdDetailsFragment(adId.toString())
                findNavController().navigate(action)
            }
        }
    }
}
