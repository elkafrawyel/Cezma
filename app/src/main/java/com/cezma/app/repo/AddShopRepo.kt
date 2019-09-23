package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.AddShopBody
import com.cezma.app.data.model.AddShopResponse
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

class AddShopRepo(
    private val retrofitApiService: RetrofitApiService,
    private val helper: PreferencesHelper
) {

    private val imageType = MediaType.parse("image/*")
    private val textType = MediaType.parse("text/plain")

    suspend fun addShop(
        addShopBody: AddShopBody,
        logo: File,
        cover: File
    ): DataResource<AddShopResponse> {
        return safeApiCall(
            call = { call(addShopBody,logo,cover) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun call(
        body: AddShopBody,
        logo: File,
        cover: File
    ): DataResource<AddShopResponse> {

        val logoBody = RequestBody.create(imageType, logo)
        val logoMultiPart = MultipartBody.Part.createFormData("logo", logo.name, logoBody)

        val coverBody = RequestBody.create(imageType, cover)
        val coverMultiPart = MultipartBody.Part.createFormData("cover", cover.name, coverBody)

        val response = retrofitApiService.addShopAsync(
            "${Constants.AUTHORIZATION_START} ${helper.token}",
            RequestBody.create(textType, body.username),
            RequestBody.create(textType, body.title),
            RequestBody.create(textType, body.short_desc),
            RequestBody.create(textType, body.long_desc),
            body.category,
            logoMultiPart,
            coverMultiPart
        ).await()
        return DataResource.Success(response)
    }
}