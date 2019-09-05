package com.cezma.app.ui.mainActivity.shopDetails

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
import com.cezma.app.data.model.Ad
import com.cezma.app.ui.adapters.AdapterAds
import com.cezma.app.utiles.ViewState
import com.cezma.app.utiles.snackBar
import com.cezma.app.utiles.snackBarWithAction
import com.cezma.app.utiles.toast
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.shop_details_fragment.*

class ShopDetailsFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = ShopDetailsFragment()
    }

    private lateinit var viewModel: ShopDetailsViewModel
    private val adapterAds = AdapterAds().also {
        it.onItemChildClickListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shop_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ShopDetailsViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onStoreDetailsResponse(it) })

        if (viewModel.userName == null) {
            arguments?.let {
                val userName = ShopDetailsFragmentArgs.fromBundle(it).userName
                viewModel.userName = userName
                viewModel.getStoreDetails()
            }
        }

        adsRv.adapter = adapterAds
        adsRv.setHasFixedSize(true)

        backImgv.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun onStoreDetailsResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                dataCl.visibility = View.GONE
                loading.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                dataCl.visibility = View.VISIBLE
                loading.visibility = View.GONE
                setStoreData()
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
            is ViewState.Error -> {
                dataCl.visibility = View.GONE
                loading.visibility = View.GONE
                activity?.snackBar(it.message, rootView)
            }
        }
    }

    private fun setStoreData() {
        val store = viewModel.store
        if (store != null) {
            Glide.with(context!!).load(store.cover).into(shopDetailsBackImage)
            Glide.with(context!!).load(store.logo).into(profileUserImage)
            shopDetailsTitle.text = store.shortDesc
            shopDetailsTv.text = store.title
        }

        if (viewModel.ads.isEmpty()){
            adsRv.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
            emptyView.text = resources.getString(R.string.emptyList)
        }else{
            adsRv.visibility = View.VISIBLE
            adapterAds.replaceData(viewModel.ads)
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.adItem -> {
                val adId = (adapter?.data as List<Ad>)[position].adId
                val action =
                    ShopDetailsFragmentDirections.actionShopDetailsFragmentToAdDetailsFragment(adId.toString())
                findNavController().navigate(action)
            }
        }
    }
}
