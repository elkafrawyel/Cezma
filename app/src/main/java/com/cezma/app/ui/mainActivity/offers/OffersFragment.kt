package com.cezma.app.ui.mainActivity.offers

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cezma.app.R
import com.cezma.app.data.model.OfferModel
import com.cezma.app.data.model.OffersActionBody
import com.cezma.app.utiles.ViewState
import com.cezma.app.utiles.snackBar
import com.cezma.app.utiles.snackBarWithAction
import com.cezma.app.utiles.toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.koraextra.app.utily.observeEvent
import kotlinx.android.synthetic.main.offers_fragment.*


class OffersFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = OffersFragment()
    }

    private lateinit var viewModel: OffersViewModel
    private val adapterOffers = AdapterOffers().also {
        it.onItemChildClickListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.offers_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OffersViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onOffersResponse(it) })
        viewModel.uiStateActions.observeEvent(this) { onActionsResponse(it) }
        backImgv.setOnClickListener {
            findNavController().navigateUp()
        }

        offersRv.setHasFixedSize(true)
        offersRv.adapter = adapterOffers

    }

    private fun onOffersResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                loading.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE
                offersRv.visibility = View.VISIBLE
                adapterOffers.replaceData(viewModel.offers)
            }
            ViewState.NoConnection -> {
                loading.visibility = View.GONE
                offersRv.visibility = View.VISIBLE
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

    private fun onActionsResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                loading.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE
                offersRv.visibility = View.VISIBLE
                onSuccess()
            }
            ViewState.NoConnection -> {
                loading.visibility = View.GONE
                offersRv.visibility = View.VISIBLE
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
                activity?.toast(it.message)
            }
        }
    }

    private fun onSuccess() {
        when (viewModel.lastAction) {
            OffersActions.ACCEPT -> {
                viewModel.offers[viewModel.offerIndex!!].isAccepted = 1
                adapterOffers.notifyItemChanged(viewModel.offerIndex!!)
                activity?.toast(viewModel.offersActionMessage)
            }
            OffersActions.REFUSE -> {
                viewModel.offers.removeAt(viewModel.offerIndex!!)
                adapterOffers.data.removeAt(viewModel.offerIndex!!)
                adapterOffers.notifyItemRemoved(viewModel.offerIndex!!)
                activity?.toast(viewModel.offersActionMessage)
            }
            null -> {

            }
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val offerModel = (adapter?.data as List<OfferModel>)[position]

        when (view?.id) {
            R.id.offerItem -> {
                viewModel.uiState.removeObservers(this)
                val action =
                    OffersFragmentDirections.actionOffersFragmentToAdDetailsFragment(offerModel.adId.toString())
                findNavController().navigate(action)
            }

            R.id.yes -> {
                viewModel.offerIndex = position
                viewModel.offersActionBody = OffersActionBody(1, offerModel.adId, offerModel.id)
                viewModel.setAction(OffersActions.ACCEPT)
            }

            R.id.no -> {
                viewModel.offerIndex = position
                viewModel.offersActionBody = OffersActionBody(0, offerModel.adId, offerModel.id)
                viewModel.setAction(OffersActions.REFUSE)
            }
        }
    }
}
