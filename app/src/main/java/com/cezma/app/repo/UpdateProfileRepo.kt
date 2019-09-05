package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.UpdateProfileBody
import com.cezma.app.data.model.UpdateProfileResponse
import com.cezma.app.data.storage.local.PreferencesHelper
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.Constants
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UpdateProfileRepo(
    private val retrofitApiService: RetrofitApiService,
    private val helper: PreferencesHelper
) {

    private val imageType = MediaType.parse("image/*")
    private val textType = MediaType.parse("text/plain")

    suspend fun updateProfile(
        updateProfileBody: UpdateProfileBody,
        avatar: File?
    ): DataResource<UpdateProfileResponse> {
        return safeApiCall(
            call = { call(updateProfileBody, avatar) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(
        updateProfileBody: UpdateProfileBody,
        avatar: File?
    ): DataResource<UpdateProfileResponse> {

        return if (avatar != null) {
            updateProfileWithImage(updateProfileBody, avatar)
        } else {
            updateProfileWithoutImage(updateProfileBody)
        }

    }

    private suspend fun updateProfileWithoutImage(updateProfileBody: UpdateProfileBody): DataResource<UpdateProfileResponse> {
        val response = retrofitApiService.updateProfileWithoutImageAsync(
            "${Constants.AUTHORIZATION_START} ${helper.token}",
            updateProfileBody
        ).await()
        return DataResource.Success(response)
    }

    private suspend fun updateProfileWithImage(
        body: UpdateProfileBody,
        avatar: File
    ): DataResource<UpdateProfileResponse> {
        val imageBody = RequestBody.create(imageType, avatar)
        val key = "avatar"
        val imageMultiPart = MultipartBody.Part.createFormData(key, avatar.name, imageBody)


        if (body.new_password != null && body.old_password != null) {
            val response = retrofitApiService.updateProfileWithImageAsync(
                "${Constants.AUTHORIZATION_START} ${helper.token}",
                RequestBody.create(textType, body.first_name),
                RequestBody.create(textType, body.last_name),
                RequestBody.create(textType, body.username),
                RequestBody.create(textType, body.email),
                RequestBody.create(textType, body.phone),
                RequestBody.create(textType, body.phonecode),
                RequestBody.create(textType, body.gender),
                RequestBody.create(textType, body.country),
                RequestBody.create(textType, body.state),
                RequestBody.create(textType, body.new_password!!),
                RequestBody.create(textType, body.old_password!!),
                RequestBody.create(textType, body.city),
                imageMultiPart
            ).await()
            return DataResource.Success(response)
        }else{
            val response = retrofitApiService.updateProfileWithImageAsync(
                "${Constants.AUTHORIZATION_START} ${helper.token}",
                RequestBody.create(textType, body.first_name),
                RequestBody.create(textType, body.last_name),
                RequestBody.create(textType, body.username),
                RequestBody.create(textType, body.email),
                RequestBody.create(textType, body.phone),
                RequestBody.create(textType, body.phonecode),
                RequestBody.create(textType, body.gender),
                RequestBody.create(textType, body.country),
                RequestBody.create(textType, body.state),
                RequestBody.create(textType, body.city),
                imageMultiPart
            ).await()
            return DataResource.Success(response)
        }
    }

}