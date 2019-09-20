package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.FavouriteActionResponse
import com.cezma.app.data.storage.local.PreferencesHelper
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.Constants
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class FavouriteActionRepo(
    private val retrofitApiService: RetrofitApiService,
    private val helper: PreferencesHelper
) {

    suspend fun favouriteAction(
        adId: String
    ): DataResource<FavouriteActionResponse> {
        return safeApiCall(
            call = { call(adId) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(
        adId: String
    ): DataResource<FavouriteActionResponse> {
        val response = retrofitApiService.favouriteActionAsync(
            "${Constants.AUTHORIZATION_START} ${helper.token}",
            adId
        ).await()
        return DataResource.Success(response)
    }

}