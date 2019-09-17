package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.AdsResponse
import com.cezma.app.data.model.LoginBody
import com.cezma.app.data.model.LoginResponse
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall
import retrofit2.Response

class LoginRepo(private val retrofitApiService: RetrofitApiService) {

    suspend fun login(
        loginBody: LoginBody
    ): DataResource<LoginResponse> {
        return safeApiCall(
            call = { call(loginBody) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(
        loginBody: LoginBody
    ): DataResource<LoginResponse> {
        val response = retrofitApiService.getLoginAsync(loginBody).await()
        return if (response.error != null)
            DataResource.Error(response.message!!)
        else
            DataResource.Success(response)
    }


}