package com.cezma.app.ui.mainActivity.chatRoom

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.cezma.app.R

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
        arguments?.let {
            val userId = ChatRoomFragmentArgs.fromBundle(it).userId
            Toast.makeText(context, userId, Toast.LENGTH_LONG).show()
        }
    }

}
