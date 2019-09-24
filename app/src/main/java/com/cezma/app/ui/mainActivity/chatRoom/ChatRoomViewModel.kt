package com.cezma.app.ui.mainActivity.chatRoom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChatRoomViewModel : AppViewModel() {

    var adId: String? = null

    private var job: Job? = null

    private var _uiState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState>
        get() = _uiState


    fun sendMessage(message: String) {
        if (NetworkUtils.isConnected()) {
            if (job?.isActive == true)
                return
            job = launchJob(message)
        } else {
            _uiState.value = ViewState.NoConnection
        }
    }

    private fun launchJob(message: String): Job {
        return scope.launch(dispatcherProvider.io) {
            runOnMainThread { _uiState.value = ViewState.Loading }

            when (val result =
                Injector.getMessagesRepo().sendMessage(adId = adId!!, message = message)) {
                is DataResource.Success -> {
                    if (result.data.status) {
                        runOnMainThread {
                            _uiState.value = ViewState.Success
                        }
                    }else{
                        runOnMainThread {
                            _uiState.value = ViewState.Error(result.data.message)
                        }
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
