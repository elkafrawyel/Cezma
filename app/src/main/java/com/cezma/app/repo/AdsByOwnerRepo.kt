package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.AdsByOwnerResponse
import com.cezma.app.data.storage.local.PreferencesHelper
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.Constants
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class AdsByOwnerRepo(
    private val retrofitApiService: RetrofitApiService,
    private val helper: PreferencesHelper
) {

    suspend fun getAds(
        ownerId: String
    ): DataResource<AdsByOwnerResponse> {
        return safeApiCall(
            call = { call(ownerId) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(
        ownerId: String
    ): DataResource<AdsByOwnerResponse> {
        return if (helper.isLoggedIn) {
            val response = retrofitApiService.getAdsByOwnerAuthAsync(
                "${Constants.AUTHORIZATION_START} ${helper.token}",
                ownerId
            ).await()
            DataResource.Success(response)

        } else {
            val response = retrofitApiService.getAdsByOwnerAsync(ownerId).await()
            DataResource.Success(response)

        }
    }

}