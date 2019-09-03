package com.cezma.app.ui.mainActivity.adDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.data.model.Ad
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AdDetailsViewModel : AppViewModel() {
    var isFavouriteAd = false

    private var job: Job? = null

    private var _uiState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState>
        get() = _uiState

    var ad: Ad? = null
    lateinit var adId: String

    fun getAd() {
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
            when (val result = Injector.getAdRepo().getAd(adId)) {
                is DataResource.Success -> {
                    ad = result.data.ad
                    runOnMainThread {
                        _uiState.value = ViewState.Success
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
        getAd()
    }

    //============================ FavouriteAction =================================
    private var _uiStateFav = MutableLiveData<ViewState>()
    val uiStateFav: LiveData<ViewState>
        get() = _uiStateFav

    fun favouriteAction() {
        if (NetworkUtils.isConnected()) {
            if (job?.isActive == true)
                return
            job = launchFavouriteActionJob()
        } else {
            _uiStateFav.value = ViewState.NoConnection
        }
    }

    private fun launchFavouriteActionJob(): Job {
        return scope.launch(dispatcherProvider.io) {
            runOnMainThread { _uiStateFav.value = ViewState.Loading }
            when (val result = Injector.getFavouriteActionRepo().favouriteAction(adId)) {
                is DataResource.Success -> {
                    ad = result.data.ad
                    runOnMainThread {
                        _uiStateFav.value = ViewState.Success
                    }
                }
                is DataResource.Error -> {
                    runOnMainThread {
                        _uiStateFav.value = ViewState.Error(result.message)
                    }
                }
            }
        }
    }
}
