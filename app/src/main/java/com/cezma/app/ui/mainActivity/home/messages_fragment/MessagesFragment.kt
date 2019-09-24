package com.cezma.app.ui.mainActivity.home.messages_fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation

import com.cezma.app.R
import com.cezma.app.data.model.MessageModel
import com.cezma.app.ui.mainActivity.MainActivity
import com.cezma.app.ui.mainActivity.home.MainHomeFragmentDirections
import com.cezma.app.utiles.CustomLoadMoreView
import com.cezma.app.utiles.ViewState
import com.cezma.app.utiles.snackBarWithAction
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.messages_fragment.*

class MessagesFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = MessagesFragment()
    }

    private lateinit var viewModel: MessagesViewModel
    private val adapterMessages = AdapterMessages().also {
        it.onItemChildClickListener = this
        it.setEnableLoadMore(true)
        it.setOnLoadMoreListener({ viewModel.getMessagesList(true) }, messagesRv)
        it.setLoadMoreView(CustomLoadMoreView())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.messages_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MessagesViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onMessageListResponse(it) })

        adapterMessages.setEnableLoadMore(true)
        messagesRv.adapter = adapterMessages
        messagesRv.setHasFixedSize(true)

    }

    private fun onMessageListResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                loading.visibility = View.VISIBLE
                emptyView.visibility = View.GONE
                messagesRv.visibility = View.GONE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE
                emptyView.visibility = View.GONE
                messagesRv.visibility = View.VISIBLE
                adapterMessages.addData(viewModel.messagesList)
                adapterMessages.loadMoreComplete()
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
                loading.visibility = View.GONE
                emptyView.visibility = View.VISIBLE
                emptyView.text = getString(R.string.emptyList)
                messagesRv.visibility = View.GONE
            }
            is ViewState.Error -> {
                loading.visibility = View.GONE
                emptyView.visibility = View.GONE
                messagesRv.visibility = View.GONE
                adapterMessages.loadMoreFail()
                activity?.snackBarWithAction(it.message, getString(R.string.retry), rootView) {
                    viewModel.refresh()
                }
            }
            ViewState.LastPage -> {
                emptyView.visibility = View.GONE
                loading.visibility = View.GONE
                try {
                    adapterMessages.loadMoreEnd()
                } catch (e: Exception) {
                }
            }
            null -> {

            }
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {

        when(view?.id){
            R.id.messageItem ->{
                val message = (adapter!!.data as List<MessageModel>)[position]

                val action =
                    MainHomeFragmentDirections.actionMainHomeFragmentToChatRoomFragment(
                        message.adId!!,
                        message.ad!!.title!!
                    )
                Navigation.findNavController(activity as MainActivity, R.id.fragment).navigate(action)
            }
        }

    }


}
