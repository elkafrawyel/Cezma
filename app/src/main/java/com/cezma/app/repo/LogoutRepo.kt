package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.LogoutResponse
import com.cezma.app.data.model.ReportAdBody
import com.cezma.app.data.model.ReportAdResponse
import com.cezma.app.data.storage.local.PreferencesHelper
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.Constants
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class LogoutRepo(
    private val retrofitApiService: RetrofitApiService,
    private val helper: PreferencesHelper
) {

    suspend fun logOut(): DataResource<LogoutResponse> {
        return safeApiCall(
            call = { call() },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(): DataResource<LogoutResponse> {
        val response = retrofitApiService.logOutAsync(
            "${Constants.AUTHORIZATION_START} ${helper.token}").await()
        return DataResource.Success(response)
    }

}