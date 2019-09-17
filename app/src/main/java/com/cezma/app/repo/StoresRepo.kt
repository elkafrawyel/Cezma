package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.StoresResponse
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class StoresRepo(
    private val retrofitApiService: RetrofitApiService) {

    suspend fun getStores(page: Int): DataResource<StoresResponse> {
        return safeApiCall(
            call = { call(page) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(page: Int): DataResource<StoresResponse> {
        val response = retrofitApiService.getStoresAsync(page).await()
        return DataResource.Success(response)
    }

}