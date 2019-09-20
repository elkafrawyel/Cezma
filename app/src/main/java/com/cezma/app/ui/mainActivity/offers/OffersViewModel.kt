package com.cezma.app.ui.mainActivity.offers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.data.model.OfferModel
import com.cezma.app.data.model.OffersActionBody
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import com.koraextra.app.utily.Event
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OffersViewModel : AppViewModel() {
    // ============================ Fields ===================================
    var offers: ArrayList<OfferModel> = arrayListOf()
    var lastAction: OffersActions? = null
    var offersActionBody: OffersActionBody? = null
    var offerIndex: Int? = null
    var offersActionMessage: String = ""
    //============================== Job =========================================

    private var job: Job? = null

    private var _uiState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState>
        get() = _uiState

    init {
        getOffers()
    }

    fun getOffers() {
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
            when (val result = Injector.getOffersRepo().getOffers(1)) {
                is DataResource.Success -> {
                    offers.clear()
                    offers.addAll(result.data.offers)
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

    private var actionsJob: Job? = null

    private var _uiStateActions = MutableLiveData<Event<ViewState>>()
    val uiStateActions: LiveData<Event<ViewState>>
        get() = _uiStateActions


    fun setAction(offersActions: OffersActions) {
        if (NetworkUtils.isConnected()) {
            if (actionsJob?.isActive == true)
                return
            lastAction = offersActions
            actionsJob = launchJob(offersActions)
        } else {
            _uiStateActions.value = Event(ViewState.NoConnection)
        }
    }

    private fun launchJob(offersActions: OffersActions): Job {
        return scope.launch(dispatcherProvider.io) {
            runOnMainThread { _uiStateActions.value = Event(ViewState.Loading) }
            when (offersActions) {
                OffersActions.ACCEPT -> {
                    when (val result = Injector.getOffersActionRepo().action(offersActionBody!!)) {
                        is DataResource.Success -> {
                            if (result.data.status) {
                                offersActionMessage = result.data.msg
                                runOnMainThread {
                                    _uiStateActions.value = Event(ViewState.Success)
                                }

                            } else {
                                runOnMainThread {
                                    _uiStateActions.value = Event(ViewState.Error(result.data.msg))
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
                OffersActions.REFUSE -> {
                    when (val result = Injector.getOffersActionRepo().action(offersActionBody!!)) {
                        is DataResource.Success -> {
                            if (result.data.status) {
                                offersActionMessage = result.data.msg
                                runOnMainThread {
                                    _uiStateActions.value = Event(ViewState.Success)
                                }
                            } else {
                                runOnMainThread {
                                    _uiStateActions.value = Event(ViewState.Error(result.data.msg))
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
            }
        }
    }

    fun refresh() {
        if (lastAction != null) {
            setAction(lastAction!!)
        }
    }
}

enum class OffersActions(val value: String) {
    ACCEPT("accept"),
    REFUSE("refuse")
}
