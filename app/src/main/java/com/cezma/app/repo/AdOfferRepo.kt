package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.AdOfferBody
import com.cezma.app.data.model.AdOfferResponse
import com.cezma.app.data.model.ReportAdBody
import com.cezma.app.data.model.ReportAdResponse
import com.cezma.app.data.storage.local.PreferencesHelper
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.Constants
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class AdOfferRepo(
    private val retrofitApiService: RetrofitApiService,
    private val helper: PreferencesHelper
) {

    suspend fun offer(adOfferBody: AdOfferBody): DataResource<AdOfferResponse> {
        return safeApiCall(
            call = { call(adOfferBody) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(adOfferBody: AdOfferBody): DataResource<AdOfferResponse> {
        val response = retrofitApiService.adOfferAsync(
            "${Constants.AUTHORIZATION_START} ${helper.token}",
            adOfferBody
        ).await()
        return DataResource.Success(response)

    }

}