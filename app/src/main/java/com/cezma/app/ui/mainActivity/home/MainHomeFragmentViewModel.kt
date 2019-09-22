package com.cezma.app.ui.mainActivity.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.data.model.NotificationModel
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import com.koraextra.app.utily.Event
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainHomeFragmentViewModel : AppViewModel() {

    var isOpened = false
    var selectedBottomItemId = 1

    private var job: Job? = null

    private var _uiState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState>
        get() = _uiState

    init {
        if (Injector.getPreferenceHelper().isLoggedIn) {
            refreshToken()
            getNotis()
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

    private var _uiStateLogOut = MutableLiveData<Event<ViewState>>()
    val uiStateLogOut: LiveData<Event<ViewState>>
        get() = _uiStateLogOut

    var logOutMessage: String = ""

    fun logOut() {
        if (NetworkUtils.isConnected()) {
            if (job?.isActive == true)
                return
            job = launchLogoutJob()
        } else {
            _uiStateLogOut.value = Event(ViewState.NoConnection)
        }
    }

    private fun launchLogoutJob(): Job {
        return scope.launch(dispatcherProvider.io) {
            runOnMainThread { _uiStateLogOut.value = Event(ViewState.Loading) }
            when (val result = Injector.getLogoutRepo().logOut()) {
                is DataResource.Success -> {
                    logOutMessage = result.data.message
                    runOnMainThread {
                        _uiStateLogOut.value = Event(ViewState.Success)
                    }
                }
                is DataResource.Error -> {
                    runOnMainThread {
                        _uiStateLogOut.value = Event(ViewState.Error(result.errorMessage))
                    }
                }
            }
        }
    }

    //================================ Notifications ==================================
    var unreadNotisCount: Int = 0

    private var jobNotifications: Job? = null

    private var _uiStateNotifications = MutableLiveData<ViewState>()
    val uiStateNotification: LiveData<ViewState>
        get() = _uiStateNotifications

    private fun getNotis() {
        if (NetworkUtils.isConnected()) {
            if (jobNotifications?.isActive == true)
                return
            jobNotifications = launchNotificationJob()

        } else {
            _uiStateNotifications.value = ViewState.NoConnection
        }
    }

    private fun launchNotificationJob(): Job {
        return scope.launch(dispatcherProvider.io) {
            when (val result =
                Injector.getNotisRepo().getNotis(1)) {
                is DataResource.Success -> {
                    //  Notifications not read count
                    unreadNotisCount = result.data.unreadcount
                    runOnMainThread {
                        _uiStateNotifications.value = ViewState.Success
                    }
                }
                is DataResource.Error -> {
                    runOnMainThread {
                        _uiStateNotifications.value = ViewState.Error(result.errorMessage)
                    }
                }
            }
        }
    }
}