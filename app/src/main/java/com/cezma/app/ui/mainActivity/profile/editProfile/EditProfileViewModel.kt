package com.cezma.app.ui.mainActivity.profile.editProfile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.data.model.*
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

class EditProfileViewModel : AppViewModel() {

    var userImagePath: String? = null
    var selectedImage: File? = null

    var userModel: UserModel? = null
    var userCounty: CountryModel? = null
    var userState: StateModel? = null
    var userCity: CityModel? = null

    var countries: ArrayList<CountryModel> = arrayListOf()
    var cities: ArrayList<CityModel> = arrayListOf()
    var states: ArrayList<StateModel> = arrayListOf()
    //=================================Load Spinner Data ==================================

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
                        _uiState.value = ViewState.Error(countriesResult.message)
                    }
                }
            }
        }
    }

    private suspend fun onCountriesSuccess(countries: List<CountryModel>) {
        this.countries.clear()
        this.countries.addAll(countries)
        countries.forEach {
            if (it.id == userModel!!.country_id) {
                userCounty = it
                return@forEach
            }
        }

        val statesResult =
            Injector.getStatesRepo().get(userModel!!.country_id.toString())

        when (statesResult) {
            is DataResource.Success -> {
                onStatesSuccess(statesResult.data.states)
            }
            is DataResource.Error -> {
                runOnMainThread {
                    _uiState.value = ViewState.Error(statesResult.message)
                }
            }
        }
    }

    private suspend fun onStatesSuccess(states: List<StateModel>) {
        this.states.clear()
        this.states.addAll(states)
        states.forEach {
            if (it.id == userModel!!.state) {
                userState = it
                return@forEach
            }
        }

        val citiesResult =
            Injector.getCitiesRepo().get(userModel!!.state.toString())

        when (citiesResult) {
            is DataResource.Success -> {
                onCitiesSuccess(citiesResult.data.cities)
            }
            is DataResource.Error -> {
                runOnMainThread {
                    _uiState.value = ViewState.Error(citiesResult.message)
                }
            }
        }
    }

    private fun onCitiesSuccess(cities: List<CityModel>) {
        this.cities.clear()
        this.cities.addAll(cities)
        cities.forEach {
            if (it.id == userModel!!.city) {
                userCity = it
                return@forEach
            }
        }

        runOnMainThread {
            _uiState.value = ViewState.Success
        }

    }

    fun refresh() {
        getSpinnersData()
    }

    //================================== Update Profile ============================================

    private var updateJob: Job? = null


    var updateMessage: String? = null
    private var _uiStateUpdateProfile = MutableLiveData<ViewState>()
    val uiStateUpdateProfile: LiveData<ViewState>
        get() = _uiStateUpdateProfile

    fun updateProfile(updateProfileBody: UpdateProfileBody, avatar: File?) {
        if (NetworkUtils.isConnected()) {
            if (updateJob?.isActive == true)
                return


            updateJob = if (avatar == null)
                launchUpdateProfileJob(updateProfileBody)
            else
                launchUpdateProfileJob(updateProfileBody, avatar)

        } else {
            _uiStateUpdateProfile.value = ViewState.NoConnection
        }
    }

    private fun launchUpdateProfileJob(
        updateProfileBody: UpdateProfileBody,
        avatar: File? = null
    ): Job {
        return scope.launch(dispatcherProvider.io) {
            runOnMainThread { _uiStateUpdateProfile.value = ViewState.Loading }

            when (val result =
                Injector.getUpdateProfileRepo().updateProfile(updateProfileBody, avatar)) {
                is DataResource.Success -> {
                    updateMessage = result.data.message
                    runOnMainThread {
                        _uiStateUpdateProfile.value = ViewState.Success
                    }
                }
                is DataResource.Error -> {
                    runOnMainThread {
                        _uiStateUpdateProfile.value = ViewState.Error(result.message)
                    }
                }
            }
        }
    }

    fun getUserImageFile(): File? {
        return if (userImagePath != null)
            File(userImagePath)
        else
            null
    }
}
