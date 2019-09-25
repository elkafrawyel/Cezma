package com.cezma.app.repo

import com.cezma.app.R
import com.cezma.app.data.model.*
import com.cezma.app.data.storage.local.PreferencesHelper
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.utiles.Constants
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.safeApiCall

class MessagesRepo(
    private val retrofitApiService: RetrofitApiService,
    private val helper: PreferencesHelper
) {

    suspend fun sendMessage(
        adId: String,
        message: String
    ): DataResource<SendMessageResponse> {
        return safeApiCall(
            call = { sendMessageCall(adId, message) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun sendMessageCall(
        adId: String,
        message: String
    ): DataResource<SendMessageResponse> {
        val response = retrofitApiService.sendMessageAsync(
            "${Constants.AUTHORIZATION_START} ${helper.token}",
            adId,
            SendMessageBody(message)
        ).await()
        return DataResource.Success(response)
    }

    suspend fun getListMessages(page: Int): DataResource<ListMessagesResponse> {
        return safeApiCall(
            call = { listMessagesCall(page) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun listMessagesCall(page: Int): DataResource<ListMessagesResponse> {
        val response = retrofitApiService.getListMessagesAsync(
            "${Constants.AUTHORIZATION_START} ${helper.token}",
            page
        ).await()
        return DataResource.Success(response)
    }

    suspend fun getBadgeCount(): DataResource<BadgeCountResponse> {
        return safeApiCall(
            call = { badgeCountCall() },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun badgeCountCall(): DataResource<BadgeCountResponse> {
        val response = retrofitApiService.getBadgeNumbersAsync(
            "${Constants.AUTHORIZATION_START} ${helper.token}"
        ).await()
        return DataResource.Success(response)
    }

    suspend fun readMessages(): DataResource<FavouriteActionResponse> {
        return safeApiCall(
            call = { readMessagesCall() },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun readMessagesCall(): DataResource<FavouriteActionResponse> {
        val response = retrofitApiService.readMessageAsync(
            "${Constants.AUTHORIZATION_START} ${helper.token}"
        ).await()
        return DataResource.Success(response)
    }

    suspend fun getChatRoom(
        page: Int,
        adId: String,
        userName: String
    ): DataResource<ChatRoomResponse> {
        return safeApiCall(
            call = { chatRoomCall(page, adId, userName) },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    private suspend fun chatRoomCall(
        page: Int,
        adId: String,
        userName: String): DataResource<ChatRoomResponse> {
        val response = retrofitApiService.getChatRoomMessagesAsync(
            "${Constants.AUTHORIZATION_START} ${helper.token}",
            adId,
            page,
            userName
        ).await()
        return DataResource.Success(response)
    }
}