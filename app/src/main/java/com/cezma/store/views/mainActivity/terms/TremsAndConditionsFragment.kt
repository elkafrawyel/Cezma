package com.cezma.store.views.mainActivity.terms

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.cezma.store.R

class TremsAndConditionsFragment : Fragment() {

    companion object {
        fun newInstance() = TremsAndConditionsFragment()
    }

    private lateinit var viewModel: TremsAndConditionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.trems_and_conditions_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TremsAndConditionsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
