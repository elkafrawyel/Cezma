package com.cezma.app.ui.mainActivity.contactUs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.data.model.ContactUsBody
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ContactUsViewModel : AppViewModel() {

    var message: String = ""
    private var job: Job? = null

    private var _uiState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState>
        get() = _uiState

    fun contactUs(contactUsBody: ContactUsBody) {
        if (NetworkUtils.isConnected()) {
            if (job?.isActive == true)
                return
            job = launchJob(contactUsBody)
        } else {
            _uiState.value = ViewState.NoConnection
        }
    }

    private fun launchJob(contactUsBody: ContactUsBody): Job {
        return scope.launch(dispatcherProvider.io) {
            runOnMainThread { _uiState.value = ViewState.Loading }

            when (val result = Injector.getContactUsRepo().contactUs(contactUsBody)) {
                is DataResource.Success -> {
                    message = result.data.message
                    runOnMainThread {
                        _uiState.value = ViewState.Success
                    }
                }
                is DataResource.Error -> {
                    runOnMainThread {
                        _uiState.value = ViewState.Error(result.errorMessage)
                    }
                }
            }
        }
    }

}
