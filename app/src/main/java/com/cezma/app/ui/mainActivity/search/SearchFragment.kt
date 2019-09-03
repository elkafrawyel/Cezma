package com.cezma.app.ui.mainActivity.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import com.cezma.app.R
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel
    val min = 100
    val max = 50000
    val total = max - min
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        ArrayAdapter.createFromResource(requireContext(), R.array.gover_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                searchCategorySpinner.adapter = adapter
            }

        ArrayAdapter.createFromResource(requireContext(), R.array.gover_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                searchSubCategorySpinner.adapter = adapter
            }

        ArrayAdapter.createFromResource(requireContext(), R.array.gover_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                searchCountrySpinner.adapter = adapter
            }

        ArrayAdapter.createFromResource(requireContext(), R.array.gover_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                searchGovernorateSpinner.adapter = adapter
            }

        startTv.text = "100"
        endTv.text = "1000"

        seekbar.setMinValue(100F)
        seekbar.setMaxValue(1000F)

        seekbar.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            minTv.text = "Min $minValue"
            maxTv.text = "Max $maxValue"
        }

    }

}
