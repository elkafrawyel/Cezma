package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.AdResponse
import com.cezma.app.data.model.AdsResponse
import com.cezma.app.data.storage.local.PreferencesHelper
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.Constants
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class AdRepo(
    private val retrofitApiService: RetrofitApiService,
    private val helper: PreferencesHelper
) {

    suspend fun getAd(
        adId: String
    ): DataResource<AdResponse> {
        return safeApiCall(
            call = { call(adId) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(
        adId: String
    ): DataResource<AdResponse> {
        return if (helper.isLoggedIn) {
            val response = retrofitApiService.getAdAuthAsync(
                "${Constants.AUTHORIZATION_START} ${helper.token}",
                adId
            ).await()
            DataResource.Success(response)

        } else {
            val response = retrofitApiService.getAdAsync(adId).await()
            DataResource.Success(response)

        }
    }

}