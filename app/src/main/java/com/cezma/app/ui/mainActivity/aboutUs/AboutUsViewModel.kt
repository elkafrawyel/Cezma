package com.cezma.app.ui.mainActivity.aboutUs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.data.model.StaticPage
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.Constants
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AboutUsViewModel : AppViewModel() {


    var page: StaticPage? = null
    private var job: Job? = null

    private var _uiState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState>
        get() = _uiState

    init {
        getPage()
    }

    private fun getPage() {
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
            when (val result = Injector.getStaticPagesRepo().getPage(Constants.StaticPages.ABOUT)) {
                is DataResource.Success -> {
                    page = result.data.page
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
