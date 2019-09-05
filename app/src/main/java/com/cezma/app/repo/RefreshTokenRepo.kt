package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.AdsResponse
import com.cezma.app.data.model.LoginBody
import com.cezma.app.data.model.LoginResponse
import com.cezma.app.data.model.RefreshTokenBody
import com.cezma.app.data.storage.local.PreferencesHelper
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class RefreshTokenRepo(
    private val retrofitApiService: RetrofitApiService,
    private val preferencesHelper: PreferencesHelper
) {

    suspend fun refresh(): DataResource<LoginResponse> {
        return safeApiCall(
            call = { call() },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(): DataResource<LoginResponse> {
        val response =
            retrofitApiService.refreshAsync(RefreshTokenBody(preferencesHelper.refreshToken))
                .await()
        return DataResource.Success(response)
    }

}