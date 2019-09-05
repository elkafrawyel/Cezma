package com.cezma.app.ui.mainActivity.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.data.model.Ad
import com.cezma.app.data.model.UserModel
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProfileViewModel : AppViewModel() {
    private var job: Job? = null

    private var _uiState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState>
        get() = _uiState

    var userModel: UserModel? = null

    var ads: ArrayList<Ad> = arrayListOf()

    init {
        getProfile()
    }

    private fun getProfile() {
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
            when (val result = Injector.getProfileRepo().get()) {
                is DataResource.Success -> {
                    userModel = result.data.userModel

                    when (val adsResult =
                        Injector.getAdsByOwnerId().getAds(userModel!!.id.toString())) {
                        is DataResource.Success -> {
                            ads.clear()
                            ads.addAll(adsResult.data.ads)
                            runOnMainThread {
                                _uiState.value = ViewState.Success
                            }
                        }
                        is DataResource.Error -> {
                            runOnMainThread {
                                _uiState.value = ViewState.Error(adsResult.message)
                            }
                        }
                    }

                }
                is DataResource.Error -> {
                    runOnMainThread {
                        _uiState.value = ViewState.Error(result.message)
                    }
                }
            }
        }
    }

    fun refresh() {
        getProfile()
    }
}
