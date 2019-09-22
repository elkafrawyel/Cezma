package com.cezma.app.ui.mainActivity.followingShops

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.data.model.StoreModel
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FollowingShopsViewModel : AppViewModel() {

    var page: Int = 0
    private var lastPage: Int = 1

    private var job: Job? = null

    private var _uiState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState>
        get() = _uiState

    var storesList: ArrayList<StoreModel> = arrayListOf()
    var allStoresList: ArrayList<StoreModel> = arrayListOf()

    init {
        getStores()
    }

    fun getStores(loadMore: Boolean = false) {
        if (NetworkUtils.isConnected()) {
            if (job?.isActive == true)
                return

            page++
            if (page <= lastPage) {
                job = launchJob(loadMore)
            } else {
                _uiState.value = ViewState.LastPage
            }

        } else {
            _uiState.value = ViewState.NoConnection
        }
    }

    private fun launchJob(loadMore: Boolean): Job {
        return scope.launch(dispatcherProvider.io) {
            if (!loadMore) {
                runOnMainThread { _uiState.value = ViewState.Loading }
            }
            when (val result = Injector.getStoresRepo().getStores(page)) {
                is DataResource.Success -> {

                    if (result.data.stores.isNotEmpty()) {
                        lastPage = result.data.pages
                        if (result.data.stores.isEmpty()) {
                            _uiState.value = ViewState.Empty
                        } else {
                            storesList.clear()
                            storesList.addAll(result.data.stores)
                            allStoresList.addAll(result.data.stores)
                        }
                        runOnMainThread {
                            _uiState.value = ViewState.Success
                        }
                    } else {
                        runOnMainThread {
                            _uiState.value = ViewState.Empty
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
        getStores()
    }


}
