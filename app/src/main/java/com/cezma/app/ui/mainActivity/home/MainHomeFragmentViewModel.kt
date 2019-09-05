package com.cezma.app.ui.mainActivity.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainHomeFragmentViewModel : AppViewModel() {

    var isOpened = false
    var selectedBottomItemId = 0

    private var job: Job? = null

    private var _uiState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState>
        get() = _uiState

    init {
        if (Injector.getPreferenceHelper().isLoggedIn) {
            refreshToken()
        }
    }

    private fun refreshToken() {
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
            when (val result = Injector.getRefreshTokenRepo().refresh()) {
                is DataResource.Success -> {
                    val helper = Injector.getPreferenceHelper()
                    helper.isLoggedIn = true
                    helper.token = result.data.accessToken
                    helper.refreshToken = result.data.refreshToken
                }
                is DataResource.Error -> {
                }
            }
        }
    }

}
