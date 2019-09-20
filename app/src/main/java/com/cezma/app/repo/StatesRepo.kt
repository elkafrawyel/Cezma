package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.StateResponse
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class StatesRepo(private val retrofitApiService: RetrofitApiService) {

    suspend fun get(countryId:String): DataResource<StateResponse> {
        return safeApiCall(
            call = { call(countryId) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(countryId: String): DataResource<StateResponse> {
        val response = retrofitApiService.getStatesAsync(countryId).await()
        return DataResource.Success(response)
    }

}