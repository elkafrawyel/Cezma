package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.CommentsBody
import com.cezma.app.data.model.CommentsResponse
import com.cezma.app.data.model.OffersResponse
import com.cezma.app.data.storage.local.PreferencesHelper
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.Constants
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class CommentsRepo(
    private val apiService: RetrofitApiService,
    private val preferencesHelper: PreferencesHelper
) {

    suspend fun getComments(adId: String): DataResource<CommentsResponse> {
        return safeApiCall(
            call = { call(adId) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(adId: String): DataResource<CommentsResponse> {
        val response = apiService.getCommentsAsync(
            "${Constants.AUTHORIZATION_START} ${preferencesHelper.token}",
            CommentsBody(adId)
        ).await()

        return DataResource.Success(response)
    }
}