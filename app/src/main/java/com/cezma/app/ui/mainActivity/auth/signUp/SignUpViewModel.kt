package com.cezma.app.ui.mainActivity.auth.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.R
import com.cezma.app.data.model.CityModel
import com.cezma.app.data.model.CountryModel
import com.cezma.app.data.model.RegisterBody
import com.cezma.app.data.model.StateModel
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SignUpViewModel : AppViewModel() {


    var userCountry: CountryModel? = null
    var userState: StateModel? = null
    var userCity: CityModel? = null
    //male
    var userGender: Int = 1

    var countries: ArrayList<CountryModel> = arrayListOf()
    var cities: ArrayList<CityModel> = arrayListOf()
    var states: ArrayList<StateModel> = arrayListOf()

    //================================== Spinner Data ====================================

    private var job: Job? = null

    private var _uiState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState>
        get() = _uiState

    fun getSpinnersData() {
        if (NetworkUtils.isConnected()) {
            if (job?.isActive == true)
                return
            job = launchJob()
        } else {
            _uiState.value = ViewState.NoConnection
        }
    }

    private fun launchJob(): Job {
        return scope.launch(dispatcherProvider.io) {
            runOnMainThread { _uiState.value = ViewState.Loading }

            when (val countriesResult = Injector.getCountriesRepo().get()) {
                is DataResource.Success -> {
                    onCountriesSuccess(countriesResult.data.countries)
                }
                is DataResource.Error -> {
                    runOnMainThread {
                        _uiState.value = ViewState.Error(countriesResult.errorMessage)
                    }
                }
            }
        }
    }

    private suspend fun onCountriesSuccess(countries: List<CountryModel>) {
        this.countries.clear()
        this.countries.addAll(countries)
        userCountry = countries[0]
        val statesResult =
            Injector.getStatesRepo().get(userCountry!!.id.toString())

        when (statesResult) {
            is DataResource.Success -> {
                onStatesSuccess(statesResult.data.states)
            }
            is DataResource.Error -> {
                runOnMainThread {
                    _uiState.value = ViewState.Error(statesResult.errorMessage)
                }
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
                        _uiState.value = ViewState.Error(citiesResult.errorMessage)
                    }
                }
            }
        } else {
            runOnMainThread {
                _uiState.value =
                    ViewState.Error(Injector.getApplicationContext().getString(R.string.generalError))
            }
        }
    }

    private fun onCitiesSuccess(cities: List<CityModel>) {
        this.cities.clear()
        this.cities.addAll(cities)

        if (cities.isNotEmpty()) {
            userCity = cities[0]

            runOnMainThread {
                _uiState.value = ViewState.Success
            }

        } else {
            runOnMainThread {
                _uiState.value =
                    ViewState.Error(Injector.getApplicationContext().getString(R.string.generalError))
            }
        }

    }

    fun refresh() {
        getSpinnersData()
    }

    //====================================== Register =======================================
    var registerSuccessMessage: String = ""

    private var _register = MutableLiveData<ViewState>()
    val registerLiveData: LiveData<ViewState>
        get() = _register

    fun register(registerBody: RegisterBody) {
        if (NetworkUtils.isConnected()) {
            if (job?.isActive == true)
                return
            job = launchRegisterJob(registerBody)
        } else {
            _register.value = ViewState.NoConnection
        }
    }

    private fun launchRegisterJob(registerBody: RegisterBody): Job {
        return scope.launch(dispatcherProvider.io) {
            runOnMainThread { _register.value = ViewState.Loading }

            when (val result = Injector.getRegisterRepo().register(registerBody)) {
                is DataResource.Success -> {
                    registerSuccessMessage = result.data.message!!
                    runOnMainThread {
                        _register.value = ViewState.Success
                    }
                }
                is DataResource.Error -> {
                    runOnMainThread {
                        _register.value = ViewState.Error(result.errorMessage)
                    }
                }
            }
        }
    }
}
