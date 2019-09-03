package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.SubCategoriesResponse
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class SubCategoriesRepo(private val retrofitApiService: RetrofitApiService) {

    suspend fun getSubCategories(categoryName:String): DataResource<SubCategoriesResponse> {
        return safeApiCall(
            call = { call(categoryName) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(categoryName:String): DataResource<SubCategoriesResponse> {
        val response = retrofitApiService.getSubCategoriesAsync(categoryName).await()
        return DataResource.Success(response)
    }

}