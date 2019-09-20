package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.AdResponse
import com.cezma.app.data.model.SlidersResponse
import com.cezma.app.data.storage.local.PreferencesHelper
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.Constants
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class SlidersRepo(
    private val retrofitApiService: RetrofitApiService) {

    suspend fun get(): DataResource<SlidersResponse> {
        return safeApiCall(
            call = { call() },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(): DataResource<SlidersResponse> {
        val response = retrofitApiService.getSlidersAsync().await()
        return DataResource.Success(response)

    }

}