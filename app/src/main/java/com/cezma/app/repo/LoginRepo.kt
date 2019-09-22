package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.LoginBody
import com.cezma.app.data.model.LoginResponse
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class LoginRepo(private val retrofitApiService: RetrofitApiService) {

    suspend fun login(
        loginBody: LoginBody
    ): DataResource<LoginResponse> {
        return safeApiCall(
            call = {
                val response = retrofitApiService.getLoginAsync(loginBody).await()
                 if (response.error != null)
                     DataResource.Error(response.message!!)
                else
                    DataResource.Success(response)
            },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }
}