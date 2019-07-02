package com.cezma.store.views.mainActivity.upgradeAccount

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.cezma.store.R

class UpgradeAccountFragment : Fragment() {

    companion object {
        fun newInstance() = UpgradeAccountFragment()
    }

    private lateinit var viewModel: UpgradeAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.upgrade_account_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UpgradeAccountViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
