package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.FavouriteActionResponse
import com.cezma.app.data.model.NotificationsResponse
import com.cezma.app.data.storage.local.PreferencesHelper
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.Constants
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class NotiRepo(
    private val retrofitApiService: RetrofitApiService,
    private val helper: PreferencesHelper
) {

    suspend fun getNotis(
        page: Int
    ): DataResource<NotificationsResponse> {
        return safeApiCall(
            call = { call(page) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(
        page: Int
    ): DataResource<NotificationsResponse> {

        val response = retrofitApiService.getNotisAsync(
            "${Constants.AUTHORIZATION_START} ${helper.token}",
            page
        ).await()
        return DataResource.Success(response)

    }
//======================================read Notification===========================
    suspend fun readNotis(): DataResource<FavouriteActionResponse> {
        return safeApiCall(
            call = { callRead() },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun callRead(): DataResource<FavouriteActionResponse> {

        val response = retrofitApiService.readNotisAsync(
            "${Constants.AUTHORIZATION_START} ${helper.token}").await()
        return DataResource.Success(response)

    }

}