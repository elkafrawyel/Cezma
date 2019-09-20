package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.HomeAdsResponse
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class HomeAdsRepo(
    private val retrofitApiService: RetrofitApiService
) {

    suspend fun getAds(
        isFeature: Int?,
        isUsed: Int?,
        pricelevel: String?,
        page: Int
    ): DataResource<HomeAdsResponse> {
        return safeApiCall(
            call = { call(isFeature, isUsed, pricelevel, page) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(
        isFeature: Int?,
        isUsed: Int?,
        pricelevel: String?,
        page: Int
    ): DataResource<HomeAdsResponse> {
        val response = retrofitApiService.getHomeAdsAsync(
            page,
            isFeature,
            isUsed,
            pricelevel
        ).await()
        return DataResource.Success(response)

    }

}