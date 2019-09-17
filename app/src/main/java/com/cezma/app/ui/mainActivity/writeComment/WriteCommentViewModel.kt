package com.cezma.app.ui.mainActivity.writeComment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.data.model.CategoryModel
import com.cezma.app.data.model.WriteCommentBody
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WriteCommentViewModel : AppViewModel() {
    var adId: String? = null
    var writeCommentBody: WriteCommentBody? = null
    var commentMessage: String = ""
    private var job: Job? = null

    private var _uiState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState>
        get() = _uiState

    fun writeComment() {
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
            when (val result = Injector.getWriteCommentRepo().send(writeCommentBody!!)) {
                is DataResource.Success -> {
                    if (result.data.status) {
                        commentMessage = result.data.msg
                        runOnMainThread {
                            _uiState.value = ViewState.Success
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
