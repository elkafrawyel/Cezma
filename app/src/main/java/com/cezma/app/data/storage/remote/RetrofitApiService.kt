package com.cezma.app.data.storage.remote

import com.cezma.app.data.model.*
import kotlinx.coroutines.Deferred
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


}