package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.CitiesRespose
import com.cezma.app.data.model.CountriesResponse
import com.cezma.app.data.model.StateResponse
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class CitiesRepo(private val retrofitApiService: RetrofitApiService) {

    suspend fun get(stateId:String): DataResource<CitiesRespose> {
        return safeApiCall(
            call = { call(stateId) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(stateId: String): DataResource<CitiesRespose> {
        val response = retrofitApiService.getCitiesAsync(stateId).await()
        return DataResource.Success(response)
    }

}