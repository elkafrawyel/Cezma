package com.cezma.store.views.mainActivity.home.messages_fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController

import com.cezma.store.R
import com.cezma.store.views.mainActivity.MainActivity
import com.cezma.store.views.mainActivity.home.MainHomeFragmentDirections
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.messages_fragment.*

class MessagesFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

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
        adapterMessages.onItemChildClickListener = this
        messagesRv.adapter = adapterMessages
        messagesRv.setHasFixedSize(true)

    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val action =
            MainHomeFragmentDirections.actionMainHomeFragmentToChatRoomFragment("test")
        Navigation.findNavController(activity as MainActivity,R.id.fragment).navigate(action)
    }


}
