package com.cezma.app.ui.mainActivity.upgradeAccount

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.cezma.app.R
import kotlinx.android.synthetic.main.upgrade_account_fragment.*

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
        upgradeAccountBackImgv.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}
