package com.cezma.app.ui.mainActivity.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer

import com.cezma.app.R
import com.cezma.app.data.model.*
import com.cezma.app.utiles.ViewState
import com.cezma.app.utiles.snackBarWithAction
import com.cezma.app.utiles.toast
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        viewModel.uiStateCategories.observe(this, Observer { onCategoriesResponse(it) })
        viewModel.uiStateSubCategories.observe(this, Observer { onSubCategoriesResponse(it) })
        viewModel.uiStateLocation.observe(this, Observer { onSpinnerDataResponse(it) })


    }

    private fun onSubCategoriesResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                loading.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE
                val adapterSubCategories =
                    ArrayAdapter<SubCategoryModel>(
                        context!!,
                        android.R.layout.simple_spinner_item,
                        viewModel.subCategories
                    )

                adapterSubCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                searchSubCategorySpinner.adapter = adapterSubCategories
                searchSubCategorySpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            viewModel.selectedSubCategory = viewModel.subCategories[position]
                        }
                    }

            }
            is ViewState.Error -> {
                loading.visibility = View.GONE
            }
            ViewState.NoConnection -> {
                loading.visibility = View.GONE
                activity?.snackBarWithAction(
                    getString(R.string.noConnection),
                    getString(R.string.retry),
                    rootView
                ) {
                    viewModel.refresh()
                }
            }

            ViewState.Empty -> {
                activity?.toast(getString(R.string.no_subcategories))
            }
        }
    }

    private fun onCategoriesResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                loading.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE

                val adapterCategories =
                    ArrayAdapter<CategoryModel>(
                        context!!,
                        android.R.layout.simple_spinner_item,
                        viewModel.categories
                    )
                adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                searchCategorySpinner.adapter = adapterCategories

                searchCategorySpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (viewModel.selectedCategory!!.id != viewModel.categories[position].id) {
                                viewModel.selectedCategory = viewModel.categories[position]
                                viewModel.getSubCategoriesData()
                            }
                        }
                    }
            }
            is ViewState.Error -> {
                loading.visibility = View.GONE
            }
            ViewState.NoConnection -> {
                loading.visibility = View.GONE
                activity?.snackBarWithAction(
                    getString(R.string.noConnection),
                    getString(R.string.retry),
                    rootView
                ) {
                    viewModel.refresh()
                }
            }
        }
    }

    private fun onSpinnerDataResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                loading.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE
                setData()
            }
            is ViewState.Error -> {
                loading.visibility = View.GONE
            }
            ViewState.NoConnection -> {
                loading.visibility = View.GONE
                activity?.snackBarWithAction(
                    getString(R.string.noConnection),
                    getString(R.string.retry),
                    rootView
                ) {
                    viewModel.refresh()
                }
            }
            null -> {

            }
        }
    }

    private fun setData() {

        val countriesAdapter =
            ArrayAdapter<CountryModel>(
                context!!,
                android.R.layout.simple_spinner_item,
                viewModel.countries
            )
        countriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        searchCountrySpinner.adapter = countriesAdapter
        if (viewModel.userCountry != null)
            searchCountrySpinner.setSelection(viewModel.countries.indexOf(viewModel.userCountry!!))

        searchCountrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (viewModel.userCountry!!.id != viewModel.countries[position].id) {
                    viewModel.userCountry = viewModel.countries[position]
                    viewModel.getSpinnersData()
                }
            }
        }

        val statesAdapter =
            ArrayAdapter<StateModel>(
                context!!,
                android.R.layout.simple_spinner_item,
                viewModel.states
            )

        statesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        searchGovernorateSpinner.adapter = statesAdapter
        if (viewModel.userState != null)
            searchGovernorateSpinner.setSelection(viewModel.states.indexOf(viewModel.userState!!))

        searchGovernorateSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (viewModel.userState!!.id != viewModel.states[position].id) {
                        viewModel.userState = viewModel.states[position]
                        viewModel.getSpinnersData()
                    }
                }
            }

        val citiesAdapter =
            ArrayAdapter<CityModel>(
                context!!,
                android.R.layout.simple_spinner_item,
                viewModel.cities
            )
        citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        searchCitySpinner.adapter = citiesAdapter
        searchCitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (viewModel.userCity!!.id != viewModel.cities[position].id) {
                    viewModel.userCity = viewModel.cities[position]
                }
            }
        }

    }
}
