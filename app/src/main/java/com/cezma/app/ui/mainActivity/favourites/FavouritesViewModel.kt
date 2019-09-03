package com.cezma.app.ui.mainActivity.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.data.model.FavoriteAd
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FavouritesViewModel : AppViewModel() {

    private var job: Job? = null

    private var _uiState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState>
        get() = _uiState

    var favouriteAds: ArrayList<FavoriteAd> = arrayListOf()

    init {
        getFavouriteAds()
    }

    private fun getFavouriteAds() {
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
            when (val result = Injector.getFavouritesAdsRepo().getFavouriteAds()) {
                is DataResource.Success -> {
                    favouriteAds.clear()
                    favouriteAds.addAll(result.data.favoriteAds)
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
        getFavouriteAds()
    }





    //============================ FavouriteAction =================================
    lateinit var adId: String


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
