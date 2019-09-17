package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.OffersResponse
import com.cezma.app.data.storage.local.PreferencesHelper
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.Constants
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class OffersRepo(
    private val apiService: RetrofitApiService,
    private val preferencesHelper: PreferencesHelper
) {

    suspend fun getOffers(page: Int): DataResource<OffersResponse> {
        return safeApiCall(
            call = { call(page) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(page: Int): DataResource<OffersResponse> {
        val response = apiService.getOffersAsync(
            "${Constants.AUTHORIZATION_START} ${preferencesHelper.token}",
            page
        ).await()

        return DataResource.Success(response)
    }
}