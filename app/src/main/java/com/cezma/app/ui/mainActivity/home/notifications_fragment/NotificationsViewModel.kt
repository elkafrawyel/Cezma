package com.cezma.app.ui.mainActivity.home.notifications_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.data.model.NotificationModel
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NotificationsViewModel : AppViewModel() {

    var page: Int = 0
    private var lastPage: Int = 1
    private var unreadNotisCount: Int = 0


    private var job: Job? = null

    private var _uiState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState>
        get() = _uiState

    var notisList: ArrayList<NotificationModel> = arrayListOf()


    fun getNotis(loadMore: Boolean = false) {
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
            when (val result =
                Injector.getNotisRepo().getNotis(page)) {
                is DataResource.Success -> {
                    if (result.data.notifications != null) {
                        if (result.data.notifications.isNotEmpty()) {

                            if(result.data.currentpage==1) {
                                when(val resultRead = Injector.getNotisRepo().readNotis()){
                                    is DataResource.Success->{
                                        // All Notifications read
                                    }
                                    is DataResource.Error->{

                                    }
                                }
                            }
                            //  Notifications not read count
                            unreadNotisCount = result.data.unreadcount

                            lastPage = result.data.pages
                            notisList.addAll(result.data.notifications)
                            runOnMainThread {
                                _uiState.value = ViewState.Success
                            }
                        } else {
                            runOnMainThread {
                                _uiState.value = ViewState.Empty
                            }
                        }
                    }else{
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
        getNotis()
    }
}
