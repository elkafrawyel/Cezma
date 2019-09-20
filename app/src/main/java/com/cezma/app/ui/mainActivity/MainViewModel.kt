package com.cezma.app.ui.mainActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import com.koraextra.app.utily.Event
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel:AppViewModel(){
//    private var job: Job? = null

//    private fun socialRepo() = Injector.getSocialRepo()
//    private fun preferencesHelper() = Injector.getPreferenceHelper()
//
//    private var _uiState = MutableLiveData<Event<ViewState>>()
//    val uiState: LiveData<Event<ViewState>>
//        get() = _uiState
//
//    fun socialLogin(body: SocialBody) {
//        if (NetworkUtils.isConnected()) {
//            if (job?.isActive == true)
//                return
//            job = launchJob(body)
//        } else {
//            _uiState.value = Event(ViewState.NoConnection)
//        }
//    }
//
//    private fun launchJob(body: SocialBody): Job {
//        return scope.launch(dispatcherProvider.io) {
//            withContext(dispatcherProvider.main) { _uiState.value = Event(ViewState.Loading) }
//
//            when (val response = socialRepo().login(body)) {
//                is DataResource.Success -> {
//                    withContext(dispatcherProvider.main) {
//                        if (response.data.status!!) {
////                            preferencesHelper().id = response.data.id!!
//                            preferencesHelper().name = body.name
//                            preferencesHelper().email = body.email
//                            preferencesHelper().token = body.api_token
//                            preferencesHelper().notiStatus = response.data.notiStatus!!
//                            preferencesHelper().notiSound = response.data.notiSound!!
//                            preferencesHelper().notiMatch = response.data.notiMatch!!
//                            preferencesHelper().isLoggedIn = true
//                            _uiState.value = Event(ViewState.Success)
//                        } else {
//                            _uiState.value = Event(ViewState.Error(response.data.ar!!))
//                        }
//                    }
//                }
//                is DataResource.Error -> {
//                    withContext(dispatcherProvider.main) {
//                        _uiState.value = Event(ViewState.Error(response.exception.message!!))
//                    }
//                }
//            }
//        }
//    }
}