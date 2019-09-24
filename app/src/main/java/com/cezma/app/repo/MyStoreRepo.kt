package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.AdsByOwnerResponse
import com.cezma.app.data.model.MyStoreResponse
import com.cezma.app.data.storage.local.PreferencesHelper
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.Constants
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class MyStoreRepo(
    private val retrofitApiService: RetrofitApiService,
    private val helper: PreferencesHelper
) {

    suspend fun get(
        userId: String
    ): DataResource<MyStoreResponse> {
        return safeApiCall(
            call = { call(userId) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(
        userId: String
    ): DataResource<MyStoreResponse> {
        val response = retrofitApiService.getMyStoreAsync(
            "${Constants.AUTHORIZATION_START} ${helper.token}",
            userId
        ).await()
        return DataResource.Success(response)
    }
}