package com.cezma.app.ui.mainActivity.followingShops

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.cezma.app.R
import com.cezma.app.data.model.StoreModel
import com.cezma.app.utiles.CustomLoadMoreView
import com.cezma.app.utiles.ViewState
import com.cezma.app.utiles.snackBar
import com.cezma.app.utiles.snackBarWithAction
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.following_shops_fragment.*

class FollowingShopsFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = FollowingShopsFragment()
    }

    private lateinit var viewModel: FollowingShopsViewModel
    private val adapterStores = AdapterFollowingShops().also {
        it.onItemChildClickListener = this
        it.setOnLoadMoreListener({ viewModel.getStores(true) }, followingShopsRv)
        it.setEnableLoadMore(true)
        it.setLoadMoreView(CustomLoadMoreView())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.following_shops_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FollowingShopsViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onStoresResponse(it) })

        followingShopsRv.adapter = adapterStores
        followingShopsRv.setHasFixedSize(true)

        followingShopBackImgv.setOnClickListener {
            findNavController().navigateUp()
        }

        followingShopTv.setOnClickListener {
            findNavController().navigate(R.id.action_followingShopsFragment_to_shopDetailsFragment)
        }
    }

    private fun onStoresResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                followingShopsRv.visibility = View.GONE
                loading.visibility = View.VISIBLE
                emptyView.visibility = View.GONE

            }
            ViewState.Success -> {
                followingShopsRv.visibility = View.VISIBLE
                loading.visibility = View.GONE
                emptyView.visibility = View.GONE

                adapterStores.replaceData(viewModel.storesList)
            }
            ViewState.NoConnection -> {
                followingShopsRv.visibility = View.GONE
                emptyView.visibility = View.GONE

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
                followingShopsRv.visibility = View.GONE
                emptyView.visibility = View.VISIBLE
                emptyView.text = resources.getString(R.string.emptyFavouriteList)
            }

            is ViewState.Error -> {
                followingShopsRv.visibility = View.GONE
                loading.visibility = View.GONE
                emptyView.visibility = View.GONE
                activity?.snackBar(it.message, rootView)
            }

            ViewState.LastPage -> {
                emptyView.visibility = View.GONE
                loading.visibility = View.GONE
                adapterStores.loadMoreEnd()
            }
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.storeItem -> {
                val store = adapter?.data as List<StoreModel>
                openStoreDetailsFragment(store[position])
            }
        }
    }

    private fun openStoreDetailsFragment(store: StoreModel) {
        val action =
            FollowingShopsFragmentDirections.actionFollowingShopsFragmentToShopDetailsFragment(store.username)
        findNavController().navigate(action)
    }

}
