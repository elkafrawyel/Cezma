package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.ReportAdBody
import com.cezma.app.data.model.ReportAdResponse
import com.cezma.app.data.storage.local.PreferencesHelper
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.Constants
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class ReportAdRepo(
    private val retrofitApiService: RetrofitApiService,
    private val helper: PreferencesHelper
) {

    suspend fun report(adId: String): DataResource<ReportAdResponse> {
        return safeApiCall(
            call = { call(adId) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(adId: String): DataResource<ReportAdResponse> {
        val response = retrofitApiService.reportAdAsync(
            "${Constants.AUTHORIZATION_START} ${helper.token}",
            ReportAdBody(adId)
        ).await()
        return DataResource.Success(response)

    }

}