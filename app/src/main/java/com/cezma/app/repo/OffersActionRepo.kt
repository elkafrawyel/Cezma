package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.*
import com.cezma.app.data.storage.local.PreferencesHelper
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.Constants
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class OffersActionRepo(
    private val retrofitApiService: RetrofitApiService,
    private val helper: PreferencesHelper
) {

    suspend fun action(offersActionBody: OffersActionBody): DataResource<OffersActionResponse> {
        return safeApiCall(
            call = { call(offersActionBody) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(offersActionBody: OffersActionBody): DataResource<OffersActionResponse> {
        val response = retrofitApiService.offersActionAsync(
            "${Constants.AUTHORIZATION_START} ${helper.token}",
            offersActionBody
        ).await()
        return DataResource.Success(response)
    }

}