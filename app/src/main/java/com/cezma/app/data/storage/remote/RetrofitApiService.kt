package com.cezma.app.data.storage.remote

import com.cezma.app.data.model.*
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface RetrofitApiService {

    @POST("auth/login")
    fun getLoginAsync(
        @Body loginBody: LoginBody
    ): Deferred<LoginResponse>

    @GET("categories")
    fun getCategoriesAsync(
    ): Deferred<CategoriesResponse>

    @GET("category/{categoryName}")
    fun getSubCategoriesAsync(
        @Path("categoryName") categoryName: String
    ): Deferred<SubCategoriesResponse>

    @GET("category/{categoryName}/{subCategoryName}")
    fun getAdsAsync(
        @Path("categoryName") categoryName: String,
        @Path("subCategoryName") subCategoryName: String
    ): Deferred<AdsResponse>

    @GET("listing/{adId}")
    fun getAdAsync(
        @Path("adId") adId: String
    ): Deferred<AdResponse>

    @GET("listing/{adId}")
    fun getAdAuthAsync(
        @Header("Authorization") token: String,
        @Path("adId") adId: String
    ): Deferred<AdResponse>

    @GET("logout")
    fun logOutAsync(
        @Header("Authorization") token: String
    ): Deferred<AdResponse>

    @GET("favoritesaction/{adId}")
    fun favouriteActionAsync(
        @Header("Authorization") token: String,
        @Path("adId") adId: String
    ): Deferred<AdResponse>

    @GET("favorites")
    fun getFavouritesAsync(
        @Header("Authorization") token: String
    ): Deferred<FavoritesAdsResponse>

    @GET("stores")
    fun getStoresAsync(
    ): Deferred<StoresResponse>

    @GET("category/{categoryName}/{subCategoryName}")
    fun getAdsAuthAsync(
        @Header("Authorization") token: String,
        @Path("categoryName") categoryName: String,
        @Path("subCategoryName") subCategoryName: String
    ): Deferred<AdsResponse>

    @GET("store/{userName}")
    fun getStoreDetailsAsync(
        @Path("userName") userName: String
    ): Deferred<StoreDetailsResponse>

    @GET("profile")
    fun getProfileAsync(
        @Header("Authorization") token: String
    ): Deferred<ProfileResponse>

    @GET("showadsbyow/{ownerId}")
    fun getAdsByOwnerAuthAsync(
        @Header("Authorization") token: String,
        @Path("ownerId") ownerId: String
    ): Deferred<AdsByOwnerResponse>

    @GET("showadsbyow/{ownerId}")
    fun getAdsByOwnerAsync(
        @Path("ownerId") ownerId: String
    ): Deferred<AdsByOwnerResponse>

    @GET("showcountries")
    fun getCountriesAsync(): Deferred<CountriesResponse>

    @GET("showstates/{countryId}")
    fun getStatesAsync(
        @Path("countryId") countryId: String
    ): Deferred<StateResponse>

    @GET("showcites/{stateId}")
    fun getCitiesAsync(
        @Path("stateId") stateId: String
    ): Deferred<CitiesRespose>

    @POST("auth/refresh")
    fun refreshAsync(
        @Body refreshTokenBody: RefreshTokenBody
    ): Deferred<LoginResponse>

    @Multipart
    @POST("profile")
    fun updateProfileWithImageAsync(
        @Header("Authorization") token: String,
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("phonecode") phonecode: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("country") country: RequestBody,
        @Part("state") state: RequestBody,
        @Part("new_password") new_password: RequestBody,
        @Part("old_password") old_password: RequestBody,
        @Part("city") city: RequestBody,
        @Part avatar: MultipartBody.Part): Deferred<UpdateProfileResponse>

    @Multipart
    @POST("profile")
    fun updateProfileWithImageAsync(
        @Header("Authorization") token: String,
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("phonecode") phonecode: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("country") country: RequestBody,
        @Part("state") state: RequestBody,
        @Part("city") city: RequestBody,
        @Part avatar: MultipartBody.Part): Deferred<UpdateProfileResponse>

    @POST("profile")
    fun updateProfileWithoutImageAsync(
        @Header("Authorization") token: String,
        @Body updateProfileBody: UpdateProfileBody
    ): Deferred<UpdateProfileResponse>

}