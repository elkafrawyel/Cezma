package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.RegisterBody
import com.cezma.app.data.model.RegisterResponse
import com.cezma.app.data.model.UpdateProfileResponse
import com.cezma.app.data.storage.local.PreferencesHelper
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.Constants
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class RegisterRepo(
    private val retrofitApiService: RetrofitApiService) {

    suspend fun register(
        registerBody: RegisterBody
    ): DataResource<RegisterResponse> {
        return safeApiCall(
            call = { call(registerBody) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(
        registerBody: RegisterBody
    ): DataResource<RegisterResponse> {

        val response = retrofitApiService.getRegisterAsync(registerBody).await()
        return DataResource.Success(response)

    }

}