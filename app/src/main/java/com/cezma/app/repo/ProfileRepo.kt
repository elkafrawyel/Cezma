package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.ProfileResponse
import com.cezma.app.data.storage.local.PreferencesHelper
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.Constants
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class ProfileRepo(
    private val retrofitApiService: RetrofitApiService,
    private val helper: PreferencesHelper
) {

    suspend fun get(): DataResource<ProfileResponse> {
        return safeApiCall(
            call = { call() },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(): DataResource<ProfileResponse> {
        val response = retrofitApiService.getProfileAsync(
            "${Constants.AUTHORIZATION_START} ${helper.token}").await()
        return DataResource.Success(response)

    }

}