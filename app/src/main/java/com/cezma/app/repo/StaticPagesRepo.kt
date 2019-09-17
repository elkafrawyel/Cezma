package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.PagesResponse
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.Constants
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class StaticPagesRepo(
    private val retrofitApiService: RetrofitApiService
) {

    suspend fun getPage(staticPages: Constants.StaticPages): DataResource<PagesResponse> {
        return safeApiCall(
            call = { call(staticPages) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(staticPages: Constants.StaticPages): DataResource<PagesResponse> {
        when (staticPages) {
            Constants.StaticPages.ABOUT -> {
                val response = retrofitApiService.aboutUsPageAsync().await()
                return DataResource.Success(response)
            }
            Constants.StaticPages.PRIVACY -> {
                val response = retrofitApiService.privacyPageAsync().await()
                return DataResource.Success(response)
            }
            Constants.StaticPages.HOW_TO_USE -> {
                val response = retrofitApiService.howToUsePageAsync().await()
                return DataResource.Success(response)
            }
            Constants.StaticPages.OPEN_SHOP -> {
                val response = retrofitApiService.howToOpenPageAsync().await()
                return DataResource.Success(response)
            }
            Constants.StaticPages.TERMS -> {
                val response = retrofitApiService.termsPageAsync().await()
                return DataResource.Success(response)
            }
        }
    }

}