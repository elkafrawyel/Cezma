package com.cezma.app.ui.mainActivity.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.data.model.LoginBody
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import com.koraextra.app.utily.Event
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel : AppViewModel() {

    var phoneVerified = true
    var phoneNumber: String? = null
    var token: String? = null
    var refreshToken: String? = null
    private var job: Job? = null

    private var _uiState = MutableLiveData<Event<ViewState>>()
    val uiState: LiveData<Event<ViewState>>
        get() = _uiState

    fun login(loginBody: LoginBody) {
        if (NetworkUtils.isConnected()) {
            if (job?.isActive == true)
                return
            job = launchJob(loginBody)
        } else {
            _uiState.value = Event(ViewState.NoConnection)
        }
    }

    private fun launchJob(loginBody: LoginBody): Job {
        return scope.launch(dispatcherProvider.io) {
            runOnMainThread { _uiState.value = Event(ViewState.Loading) }
            when (val result = Injector.getLoginRepo().login(loginBody)) {
                is DataResource.Success -> {
                    token = result.data.accessToken
                    refreshToken = result.data.refreshToken

                    when (val profileResult = Injector.getProfileRepo().get()) {
                        is DataResource.Success -> {
                            this@LoginViewModel.phoneVerified =
                                profileResult.data.userModel?.phone_verfied == 1
                            if (profileResult.data.userModel?.phone != null) {
                                this@LoginViewModel.phoneNumber =
                                    profileResult.data.userModel.phone.toString()
                            }

                            runOnMainThread {
                                _uiState.value = Event(ViewState.Success)
                            }
                        }
                        is DataResource.Error -> {
                            runOnMainThread {
                                _uiState.value = Event(ViewState.Error(profileResult.errorMessage))
                            }
                        }
                    }
                }
                is DataResource.Error -> {
                    runOnMainThread {
                        _uiState.value = Event(ViewState.Error(result.errorMessage))
                    }
                }
            }
        }
    }

    fun refresh(loginBody: LoginBody) {
        login(loginBody)
    }
}
