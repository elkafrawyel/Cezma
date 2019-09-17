package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.*
import com.cezma.app.data.storage.local.PreferencesHelper
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.Constants
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class WriteCommentRepo(
    private val retrofitApiService: RetrofitApiService,
    private val preferencesHelper: PreferencesHelper
) {

    suspend fun send(
        writeCommentBody: WriteCommentBody
    ): DataResource<WriteCommentResponse> {
        return safeApiCall(
            call = { call(writeCommentBody) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(
        writeCommentBody: WriteCommentBody
    ): DataResource<WriteCommentResponse> {
        val response = retrofitApiService.sentCommentWithReviewAsync(
            "${Constants.AUTHORIZATION_START} ${preferencesHelper.token}",
            writeCommentBody).await()
        return DataResource.Success(response)
    }
}