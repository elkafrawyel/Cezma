package com.cezma.app.ui.mainActivity.home.notifications_fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController

import com.cezma.app.R
import com.cezma.app.data.model.NotificationModel
import com.cezma.app.utiles.CustomLoadMoreView
import com.cezma.app.utiles.ViewState
import com.cezma.app.utiles.snackBarWithAction
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.notifications_fragment.*
import java.lang.Exception

class NotificationsFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = NotificationsFragment()
    }

    private lateinit var viewModel: NotificationsViewModel
    private val adapterNotis = AdapterNotifications().also {
        it.onItemChildClickListener = this
        it.setOnLoadMoreListener({ viewModel.getNotis(true) },  notificationsRv)
        it.setEnableLoadMore(true)
        it.setLoadMoreView(CustomLoadMoreView())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.notifications_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onNotisResponse(it) })

        if (viewModel.notisList.isEmpty()) {
            viewModel.getNotis()
        }


        adapterNotis.setEnableLoadMore(true)
         notificationsRv.adapter = adapterNotis
         notificationsRv.setHasFixedSize(true)

            }

    private fun onNotisResponse(it: ViewState?) {
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
                    adapterNotis.loadMoreEnd()
                }catch (e:Exception){
                    println("error: {${e.message}}")
                }
            }
            null -> {

            }
        }
    }

    private fun onLoading() {
        loading.visibility = View.VISIBLE
        emptyView.visibility = View.GONE
         notificationsRv.visibility = View.GONE

    }

    private fun onSuccess() {
        loading.visibility = View.GONE
        emptyView.visibility = View.GONE
         notificationsRv.visibility = View.VISIBLE
        adapterNotis.loadMoreComplete()
        adapterNotis.replaceData(viewModel.notisList)
    }

    private fun onError(message: String) {
        loading.visibility = View.GONE
        emptyView.visibility = View.GONE
         notificationsRv.visibility = View.GONE
        activity?.snackBarWithAction(message, getString(R.string.retry), rootView) {
            viewModel.refresh()
        }
    }

    private fun onEmpty() {
        loading.visibility = View.GONE
        emptyView.visibility = View.VISIBLE
        emptyView.text = getString(R.string.emptyList)
         notificationsRv.visibility = View.GONE
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.notis_item -> {
                val adId = (adapter?.data as List<NotificationModel>)[position].adId
                if(adId != "0"){
//                    val action =
//                        NotificationsFragmentDirections.actionNotificationsFragmentToAdDetailsFragment(adId.toString())
                    val bundle = Bundle()
                    bundle.putString("adId", adId)
                    activity?.findNavController(R.id.fragment)!!.navigate(R.id.adDetailsFragment,bundle)
                }
            }
        }
    }
}
