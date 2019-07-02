package com.cezma.store.views.mainActivity.home.messages_fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.cezma.store.R
import kotlinx.android.synthetic.main.messages_fragment.*

class MessagesFragment : Fragment() {

    companion object {
        fun newInstance() = MessagesFragment()
    }

    private lateinit var viewModel: MessagesViewModel
    private val adapterMessages = AdapterMessages()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.messages_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MessagesViewModel::class.java)


        val messages = ArrayList<String>()
        messages.add("a")
        messages.add("a")
        messages.add("a")
        messages.add("a")
        messages.add("a")
        messages.add("a")
        messages.add("a")

        adapterMessages.replaceData(messages)
        messagesRv.adapter = adapterMessages
        messagesRv.setHasFixedSize(true)

    }

}
