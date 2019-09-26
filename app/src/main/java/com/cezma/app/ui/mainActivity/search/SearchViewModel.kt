package com.cezma.app.ui.mainActivity.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.R
import com.cezma.app.data.model.*
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel : AppViewModel() {
    var categories: ArrayList<CategoryModel> = arrayListOf()
    var subCategories: ArrayList<SubCategoryModel> = arrayListOf()

    var selectedCategory: CategoryModel? = null
    var selectedSubCategory: SubCategoryModel? = null

    private var jobCategories: Job? = null
    private var jobSubCategories: Job? = null

    private var _uiState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState>
        get() = _uiState

    private var _uiStateCategories = MutableLiveData<ViewState>()
    val uiStateCategories: LiveData<ViewState>
        get() = _uiStateCategories

    private var _uiStateSubCategories = MutableLiveData<ViewState>()
    val uiStateSubCategories: LiveData<ViewState>
        get() = _uiStateSubCategories

    private fun getCategoriesData() {
        if (NetworkUtils.isConnected()) {
            if (jobCategories?.isActive == true)
                return
            jobCategories = launchCategoriesJob()
        } else {
            _uiStateCategories.value = ViewState.NoConnection
        }
    }

    private fun launchCategoriesJob(): Job {
        return scope.launch(dispatcherProvider.io) {
            runOnMainThread { _uiStateCategories.value = ViewState.Loading }

            when (val result = Injector.getCategoriesRepo().getCategories()) {
                is DataResource.Success -> {
                    if (result.data.categories.isNotEmpty()) {
                        categories.clear()
                        categories.addAll(result.data.categories)

                        selectedCategory = categories[0]

                        runOnMainThread {
                            _uiStateCategories.value = ViewState.Success
                        }

                        getSubCategoriesData()
                    }
                }
                is DataResource.Error -> {
                    runOnMainThread {
                        _uiStateCategories.value = ViewState.Error(result.errorMessage)
                    }
                }
            }
        }
    }

    fun getSubCategoriesData() {
        if (NetworkUtils.isConnected()) {
            if (jobSubCategories?.isActive == true)
                return
            jobSubCategories = launchSubCategories()
        } else {
            _uiStateSubCategories.value = ViewState.NoConnection
        }
    }

    private fun launchSubCategories(): Job {
        return scope.launch(dispatcherProvider.io) {
            runOnMainThread { _uiStateSubCategories.value = ViewState.Loading }

            when (val result =
                Injector.getSubCategoriesRepo()
                    .getSubCategories(selectedCategory!!.categorySlug!!)) {
                is DataResource.Success -> {
                    if (result.data.subCategories.isNotEmpty()) {

                        subCategories.clear()
                        subCategories.addAll(result.data.subCategories)

                        if (subCategories.isNotEmpty()) {
                            selectedSubCategory = subCategories[0]

                            runOnMainThread {
                                _uiStateSubCategories.value = ViewState.Success
                            }
                        } else {
                            _uiStateSubCategories.value = ViewState.Empty
                        }
                    }
                }
                is DataResource.Error -> {
                    runOnMainThread {
                        _uiStateSubCategories.value = ViewState.Error(result.errorMessage)
                    }
                }
            }
        }
    }

    fun refresh() {
        getCategoriesData()
        getSpinnersData()
    }


    var countries: ArrayList<CountryModel> = arrayListOf()
    var cities: ArrayList<CityModel> = arrayListOf()
    var states: ArrayList<StateModel> = arrayListOf()

    //================================== Spinner Data ====================================

    var userCountry: CountryModel? = null
    var userState: StateModel? = null
    var userCity: CityModel? = null

    private var jobLocation: Job? = null

    private var _uiStateLocation = MutableLiveData<ViewState>()
    val uiStateLocation: LiveData<ViewState>
        get() = _uiStateLocation

    fun getSpinnersData() {
        if (NetworkUtils.isConnected()) {
            if (jobLocation?.isActive == true)
                return
            jobLocation = launchLocationJob()
        } else {
            _uiStateLocation.value = ViewState.NoConnection
        }
    }

    private fun launchLocationJob(): Job {
        return scope.launch(dispatcherProvider.io) {
            runOnMainThread { _uiStateLocation.value = ViewState.Loading }

            when (val countriesResult = Injector.getCountriesRepo().get()) {
                is DataResource.Success -> {
                    onCountriesSuccess(countriesResult.data.countries)
                }
                is DataResource.Error -> {
                    runOnMainThread {
                        _uiStateLocation.value = ViewState.Error(countriesResult.errorMessage)
                    }
                }
            }
        }
    }

    private suspend fun onCountriesSuccess(countries: List<CountryModel>) {
        this.countries.clear()
        this.countries.addAll(countries)
        if (countries.isNotEmpty()) {
            if (userCountry == null)
                userCountry = countries[0]
            val statesResult =
                Injector.getStatesRepo().get(userCountry!!.id.toString())

            when (statesResult) {
                is DataResource.Success -> {
                    onStatesSuccess(statesResult.data.states)
                }
                is DataResource.Error -> {
                    runOnMainThread {
                        _uiStateLocation.value = ViewState.Error(statesResult.errorMessage)
                    }
                }
            }
        } else {
            runOnMainThread {
                _uiStateLocation.value =
                    ViewState.Error(Injector.getApplicationContext().getString(R.string.generalError))
            }
        }
    }

    private suspend fun onStatesSuccess(states: List<StateModel>) {
        this.states.clear()
        this.states.addAll(states)

        if (states.isNotEmpty()) {

            if (userState == null)
                userState = states[0]

            val citiesResult =
                Injector.getCitiesRepo().get(userState!!.id.toString())

            when (citiesResult) {
                is DataResource.Success -> {
                    onCitiesSuccess(citiesResult.data.cities)
                }
                is DataResource.Error -> {
                    runOnMainThread {
                        _uiStateLocation.value = ViewState.Error(citiesResult.errorMessage)
                    }
                }
            }
        } else {
            runOnMainThread {
                _uiStateLocation.value =
                    ViewState.Error(Injector.getApplicationContext().getString(R.string.generalError))
            }
        }
    }

    private fun onCitiesSuccess(cities: List<CityModel>) {
        this.cities.clear()
        this.cities.addAll(cities)
        userCity = cities[0]
        if (cities.isNotEmpty()) {
            runOnMainThread {
                _uiStateLocation.value = ViewState.Success
            }
        } else {
            runOnMainThread {
                _uiStateLocation.value =
                    ViewState.Error(Injector.getApplicationContext().getString(R.string.generalError))
            }
        }

    }


    init {
        getCategoriesData()
        getSpinnersData()
    }

}
