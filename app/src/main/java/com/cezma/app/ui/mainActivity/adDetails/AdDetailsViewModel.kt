package com.cezma.app.ui.mainActivity.adDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.data.model.Ad
import com.cezma.app.data.model.AdOfferBody
import com.cezma.app.data.model.WriteCommentBody
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import com.koraextra.app.utily.Event
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AdDetailsViewModel : AppViewModel() {
    // ============================ Fields ===================================
    var ad: Ad? = null
    lateinit var adId: String
    var adOfferBody: AdOfferBody? = null
    var writeCommentBody: WriteCommentBody? = null
    var reportMessage: String = ""
    var offerMessage: String = ""
    var commentMessage: String = ""
    var favMessage: String = ""
    var isFavouriteAd = false
    var lastAction: AdActions? = null
    //============================== Job =========================================

    private var job: Job? = null

    private var _uiState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState>
        get() = _uiState

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
                        _uiState.value = ViewState.Error(result.errorMessage)
                    }
                }
            }
        }
    }

    private var _uiStateActions = MutableLiveData<Event<ViewState>>()
    val uiStateActions: LiveData<Event<ViewState>>
        get() = _uiStateActions


    fun setAction(adActions: AdActions) {
        if (NetworkUtils.isConnected()) {
            if (job?.isActive == true)
                return
            lastAction = adActions
            job = launchJob(adActions)
        } else {
            _uiStateActions.value = Event(ViewState.NoConnection)
        }
    }

    private fun launchJob(adActions: AdActions): Job {
        return scope.launch(dispatcherProvider.io) {
            runOnMainThread { _uiStateActions.value = Event(ViewState.Loading) }
            when (adActions) {
                AdActions.Fav -> {
                    when (val result = Injector.getFavouriteActionRepo().favouriteAction(adId)) {
                        is DataResource.Success -> {
                            if (result.data.status) {
                                favMessage = result.data.message
                                runOnMainThread {
                                    _uiStateActions.value = Event(ViewState.Success)
                                }
                            } else {
                                runOnMainThread {
                                    _uiStateActions.value =
                                        Event(ViewState.Error(result.data.message))
                                }
                            }
                        }
                        is DataResource.Error -> {
                            runOnMainThread {
                                _uiStateActions.value = Event(ViewState.Error(result.errorMessage))
                            }
                        }
                    }
                }
                AdActions.Report -> {
                    when (val result = Injector.getReportAdRepo().report(adId)) {
                        is DataResource.Success -> {
                            reportMessage = result.data.msg
                            runOnMainThread {
                                _uiStateActions.value = Event(ViewState.Success)
                            }
                        }
                        is DataResource.Error -> {
                            runOnMainThread {
                                _uiStateActions.value = Event(ViewState.Error(result.errorMessage))
                            }
                        }
                    }
                }
                AdActions.Offer -> {
                    when (val result = Injector.getAdOfferRepo().offer(adOfferBody!!)) {
                        is DataResource.Success -> {
                            offerMessage = result.data.msg
                            runOnMainThread {
                                _uiStateActions.value = Event(ViewState.Success)
                            }
                        }
                        is DataResource.Error -> {
                            runOnMainThread {
                                _uiStateActions.value = Event(ViewState.Error(result.errorMessage))
                            }
                        }
                    }
                }
                AdActions.COMMENT -> {
                    when (val result = Injector.getWriteCommentRepo().send(writeCommentBody!!)) {
                        is DataResource.Success -> {
                            commentMessage = result.data.msg
                            runOnMainThread {
                                _uiStateActions.value = Event(ViewState.Success)
                            }
                        }
                        is DataResource.Error -> {
                            runOnMainThread {
                                _uiStateActions.value = Event(ViewState.Error(result.errorMessage))
                            }
                        }
                    }
                }
            }
        }
    }

    fun refresh() {
        if (lastAction != null) {
            setAction(lastAction!!)
        }
    }
}

enum class AdActions(val value: String) {
    Fav("fav"),
    Report("Report"),
    Offer("offer"),
    COMMENT("comment")
}