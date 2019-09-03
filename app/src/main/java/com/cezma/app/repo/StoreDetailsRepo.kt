package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.StoreDetailsResponse
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class StoreDetailsRepo(
    private val retrofitApiService: RetrofitApiService
) {

    suspend fun get(userName: String): DataResource<StoreDetailsResponse> {
        return safeApiCall(
            call = { call(userName) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(userName: String): DataResource<StoreDetailsResponse> {
        val response = retrofitApiService.getStoreDetailsAsync(userName).await()
        return DataResource.Success(response)
    }

}