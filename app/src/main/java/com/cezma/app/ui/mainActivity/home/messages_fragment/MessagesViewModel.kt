package com.cezma.app.ui.mainActivity.home.messages_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.data.model.MessageModel
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MessagesViewModel : AppViewModel() {


    var page: Int = 0
    private var lastPage: Int = 1

    private var job: Job? = null

    private var _uiState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState>
        get() = _uiState

    var messagesList: ArrayList<MessageModel> = arrayListOf()
    var allMessages: ArrayList<MessageModel> = arrayListOf()

    fun getMessagesList(loadMore: Boolean = false) {
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
                Injector.getMessagesRepo().getListMessages(page)) {
                is DataResource.Success -> {
                    if (result.data.messages.isNotEmpty()) {
                        lastPage = result.data.pages
                        messagesList.clear()
                        messagesList.addAll(result.data.messages)
                        allMessages.addAll(result.data.messages)


                        if (page == 1)
                            Injector.getMessagesRepo().readMessages()

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
        page = 0
        lastPage = 1
        getMessagesList()
    }
}
