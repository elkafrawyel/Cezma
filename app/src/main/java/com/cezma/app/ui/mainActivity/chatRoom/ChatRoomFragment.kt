package com.cezma.app.ui.mainActivity.chatRoom

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.cezma.app.R
import com.cezma.app.utiles.CustomLoadMoreView
import com.cezma.app.utiles.ViewState
import com.cezma.app.utiles.snackBarWithAction
import com.cezma.app.utiles.toast
import kotlinx.android.synthetic.main.chat_room_fragment.*

class ChatRoomFragment : Fragment() {

    private lateinit var viewModel: ChatRoomViewModel

    private var adapter = AdapterChatRoom(arrayListOf()).also {
        it.setEnableLoadMore(true)
        it.setOnLoadMoreListener({ viewModel.getMessages(true) }, messagesRv)
        it.setLoadMoreView(CustomLoadMoreView())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.chat_room_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ChatRoomViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onSendMessageResponse(it) })
        viewModel.uiStateMessages.observe(this, Observer { onMessagesResponse(it) })
        arguments?.let {
            val adId = ChatRoomFragmentArgs.fromBundle(it).adId
            viewModel.adId = adId

            val adTitle = ChatRoomFragmentArgs.fromBundle(it).adTitle
            chatRoomAdTitleTv.text = adTitle

            val userName = ChatRoomFragmentArgs.fromBundle(it).userName
            viewModel.userName = userName

            viewModel.getMessages()
        }

        backImgv.setOnClickListener {
            findNavController().navigateUp()
        }

        sendMessageImg.setOnClickListener {
            if (writeMessageEt.text.isEmpty())
                return@setOnClickListener
            else {
                viewModel.sendMessage(writeMessageEt.text.toString().trim())
            }
        }

        adapter.setEnableLoadMore(true)
        messagesRv.adapter = adapter
        messagesRv.setHasFixedSize(true)
    }

    private fun onMessagesResponse(it: ViewState?) {
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
                adapter.addData(viewModel.messagesList)
                adapter.loadMoreComplete()
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
                adapter.loadMoreFail()
                activity?.snackBarWithAction(it.message, getString(R.string.retry), rootView) {
                    viewModel.refresh()
                }
            }
            ViewState.LastPage -> {
                emptyView.visibility = View.GONE
                loading.visibility = View.GONE
                try {
                    adapter.loadMoreEnd()
                } catch (e: Exception) {
                }
            }
            null -> {

            }
        }
    }

    private fun onSendMessageResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {

            }

            ViewState.Success -> {
                writeMessageEt.setText("")
            }
            is ViewState.Error -> {
                activity?.toast(it.message)
            }
        }
    }

}
