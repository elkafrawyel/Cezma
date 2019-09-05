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
                    val helper = Injector.getPreferenceHelper()
                    helper.isLoggedIn = true
                    helper.token = result.data.accessToken
                    helper.refreshToken= result.data.refreshToken
                    runOnMainThread {
                        _uiState.value = Event(ViewState.Success)
                    }
                }
                is DataResource.Error -> {
                    runOnMainThread {
                        _uiState.value = Event(ViewState.Error(result.message))
                    }
                }
            }
        }
    }

    fun refresh(loginBody: LoginBody) {
        login(loginBody)
    }
}
