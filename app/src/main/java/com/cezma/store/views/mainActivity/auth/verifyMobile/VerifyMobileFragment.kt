package com.cezma.store.views.mainActivity.auth.verifyMobile

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.cezma.store.R

class VerifyMobileFragment : Fragment() {

    companion object {
        fun newInstance() = VerifyMobileFragment()
    }

    private lateinit var viewModel: VerifyMobileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.verify_mobile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(VerifyMobileViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
