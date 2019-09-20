package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.ContactUsBody
import com.cezma.app.data.model.ContactUsResponse
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class ContactUsRepo(
    private val retrofitApiService: RetrofitApiService
) {

    suspend fun contactUs(
        contactUsBody: ContactUsBody
    ): DataResource<ContactUsResponse> {
        return safeApiCall(
            call = { call(contactUsBody) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(
        contactUsBody: ContactUsBody
    ): DataResource<ContactUsResponse> {
        val response = retrofitApiService.contactUsAsync(contactUsBody).await()
        return DataResource.Success(response)
    }
}