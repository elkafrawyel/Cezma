package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.CategoriesResponse
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class CategoriesRepo(private val retrofitApiService: RetrofitApiService) {

    suspend fun getCategories(): DataResource<CategoriesResponse> {
        return safeApiCall(
            call = { call() },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(): DataResource<CategoriesResponse> {
        val response = retrofitApiService.getCategoriesAsync().await()
        return DataResource.Success(response)
    }

}