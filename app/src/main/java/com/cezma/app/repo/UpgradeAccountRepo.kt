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

class UpgradeAccountRepo(
    private val retrofitApiService: RetrofitApiService,
    private val helper: PreferencesHelper
) {

    suspend fun upgradeAccount(): DataResource<String> {
        return safeApiCall(
            call = { call() },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(): DataResource<String> {

        val response = retrofitApiService.upgradeAccountAsync(
            "${Constants.AUTHORIZATION_START} ${helper.token}"
        ).await()
        return DataResource.Success(response)

    }

}