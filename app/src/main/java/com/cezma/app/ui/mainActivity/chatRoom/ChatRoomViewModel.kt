package com.cezma.app.ui.mainActivity.chatRoom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.data.model.ChatRoomMessageModel
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChatRoomViewModel : AppViewModel() {

    var adId: String? = null
    var userName: String? = null

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
                    } else {
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


    //===========================================================================================

    var page: Int = 0
    private var lastPage: Int = 1


    private var jobMessages: Job? = null

    private var _uiStateMessages = MutableLiveData<ViewState>()
    val uiStateMessages: LiveData<ViewState>
        get() = _uiStateMessages

    var messagesList: ArrayList<ChatRoomMessageModel> = arrayListOf()
    var allMessages: ArrayList<ChatRoomMessageModel> = arrayListOf()


    fun getMessages(loadMore: Boolean = false) {
        if (NetworkUtils.isConnected()) {
            if (jobMessages?.isActive == true)
                return
            page++
            if (page <= lastPage) {
                jobMessages = launchMessagesJob(loadMore)
            } else {
                _uiStateMessages.value = ViewState.LastPage
            }

        } else {
            _uiStateMessages.value = ViewState.NoConnection
        }
    }

    private fun launchMessagesJob(loadMore: Boolean): Job {
        return scope.launch(dispatcherProvider.io) {
            if (!loadMore) {
                runOnMainThread { _uiStateMessages.value = ViewState.Loading }
            }
            when (val result = Injector.getMessagesRepo().getChatRoom(page, adId!!, userName!!)) {
                is DataResource.Success -> {
                    if (result.data.messages.isNotEmpty()) {
                        lastPage = result.data.pages
                        messagesList.clear()
                        messagesList.addAll(result.data.messages)
                        allMessages.addAll(result.data.messages)
                        runOnMainThread {
                            _uiStateMessages.value = ViewState.Success
                        }
                    } else {
                        runOnMainThread {
                            _uiStateMessages.value = ViewState.Empty
                        }
                    }
                }
                is DataResource.Error -> {
                    runOnMainThread {
                        _uiStateMessages.value = ViewState.Error(result.errorMessage)
                    }
                }
            }
        }
    }

    fun refresh() {
        page = 0
        lastPage = 1
        getMessages()
    }
}
