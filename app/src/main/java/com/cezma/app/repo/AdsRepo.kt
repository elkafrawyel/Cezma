package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.AdsResponse
import com.cezma.app.data.storage.local.PreferencesHelper
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.Constants
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class AdsRepo(
    private val retrofitApiService: RetrofitApiService,
    private val helper: PreferencesHelper
) {

    suspend fun getAds(
        categoryName: String,
        subCategoryName: String,
        page: Int
    ): DataResource<AdsResponse> {
        return safeApiCall(
            call = { call(categoryName, subCategoryName,page) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(
        categoryName: String,
        subCategoryName: String,
        page: Int
    ): DataResource<AdsResponse> {
        return if (helper.isLoggedIn) {
            val response = retrofitApiService.getAdsAuthAsync(
                "${Constants.AUTHORIZATION_START} ${helper.token}",
                categoryName,
                subCategoryName,
                page
            ).await()
            DataResource.Success(response)

        } else {
            val response = retrofitApiService.getAdsAsync(categoryName, subCategoryName,page).await()
            DataResource.Success(response)

        }
    }

}