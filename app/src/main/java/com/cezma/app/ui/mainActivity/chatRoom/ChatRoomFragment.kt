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
import com.cezma.app.utiles.ViewState
import com.cezma.app.utiles.toast
import kotlinx.android.synthetic.main.chat_room_fragment.*

class ChatRoomFragment : Fragment() {

    companion object {
        fun newInstance() = ChatRoomFragment()
    }

    private lateinit var viewModel: ChatRoomViewModel

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

        arguments?.let {
            val adId = ChatRoomFragmentArgs.fromBundle(it).adId
            val adTitle = ChatRoomFragmentArgs.fromBundle(it).adTitle
            chatRoomAdTitleTv.text = adTitle
            viewModel.adId = adId
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
