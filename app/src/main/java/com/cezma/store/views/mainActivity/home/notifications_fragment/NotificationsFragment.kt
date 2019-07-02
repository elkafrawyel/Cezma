package com.cezma.store.views.mainActivity.home.notifications_fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.cezma.store.R
import kotlinx.android.synthetic.main.notifications_fragment.*

class NotificationsFragment : Fragment() {

    companion object {
        fun newInstance() = NotificationsFragment()
    }

    private lateinit var viewModel: NotificationsViewModel
    private val  adapterNotifications = AdapterNotifications()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.notifications_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NotificationsViewModel::class.java)

        val notifications = ArrayList<String>()
        notifications.add("a")
        notifications.add("a")
        notifications.add("a")
        notifications.add("a")
        notifications.add("a")
        notifications.add("a")
        notifications.add("a")
        notifications.add("a")


        adapterNotifications.replaceData(notifications)
        notificationsRv.adapter = adapterNotifications
        notificationsRv.setHasFixedSize(true)
    }

}
