package com.cezma.app.ui.mainActivity.shopDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.data.model.Ad
import com.cezma.app.data.model.StoreModel
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ShopDetailsViewModel : AppViewModel() {

    private var job: Job? = null

    private var _uiState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState>
        get() = _uiState

    var store: StoreModel? = null
    var userName: String? = null
    var ads: ArrayList<Ad> = arrayListOf()

    fun getStoreDetails() {
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
            when (val result = Injector.getStoreDetailsRepo().get(userName!!)) {
                is DataResource.Success -> {
                    store = result.data.store

                    when (val adsResult =
                        Injector.getAdsByOwnerId().getAds(store!!.ownerId.toString())) {
                        is DataResource.Success -> {
                            ads.clear()
                            ads.addAll(adsResult.data.ads)
                            runOnMainThread {
                                _uiState.value = ViewState.Success
                            }
                        }
                        is DataResource.Error -> {
                            runOnMainThread {
                                _uiState.value = ViewState.Error(adsResult.errorMessage)
                            }
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

    fun refresh() {
        getStoreDetails()
    }
}
