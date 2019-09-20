package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.CountriesResponse
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class CountriesRepo(private val retrofitApiService: RetrofitApiService) {

    suspend fun get(): DataResource<CountriesResponse> {
        return safeApiCall(
            call = { call() },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(): DataResource<CountriesResponse> {
        val response = retrofitApiService.getCountriesAsync().await()
        return DataResource.Success(response)
    }

}