package com.cezma.app.data.storage.remote

import com.cezma.app.data.model.*
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface RetrofitApiService {

    @GET("gethome")
    fun getHomeAdsAsync(
        @Query("page") page: Int,
        @Query("is_featured") is_featured: Int?,
        @Query("is_used") is_used: Int?,
        @Query("pricelevel") priceLevel: String?
    ): Deferred<HomeAdsResponse>


    @POST("auth/login")
    fun getLoginAsync(
        @Body loginBody: LoginBody
    ): Deferred<LoginResponse>

    @POST("auth/register")
    fun getRegisterAsync(
        @Body registerBody: RegisterBody
    ): Deferred<RegisterResponse>

    @POST("contactn")
    fun contactUsAsync(
        @Body contactUsBody: ContactUsBody
    ): Deferred<ContactUsResponse>

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
        @Path("subCategoryName") subCategoryName: String,
        @Query("page") page: Int
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


    @GET("getsliders")
    fun getSlidersAsync(
    ): Deferred<SlidersResponse>


    @POST("logout")
    fun logOutAsync(
        @Header("Authorization") token: String
    ): Deferred<LogoutResponse>

    @POST("verifyuser")
    fun verifyUserAsync(
        @Header("Authorization") token: String
    ): Deferred<FavouriteActionResponse>

    @GET("favoritesaction/{adId}")
    fun favouriteActionAsync(
        @Header("Authorization") token: String,
        @Path("adId") adId: String
    ): Deferred<FavouriteActionResponse>

    @POST("sendreview")
    fun writeCommentAsync(
        @Header("Authorization") token: String,
        @Body writeCommentBody: WriteCommentBody
    ): Deferred<WriteCommentResponse>

    @GET("favorites")
    fun getFavouritesAsync(
        @Header("Authorization") token: String
    ): Deferred<FavoritesAdsResponse>

    @GET("listoffers")
    fun getOffersAsync(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): Deferred<OffersResponse>

    @POST("listcomments")
    fun getCommentsAsync(
        @Header("Authorization") token: String,
        @Body commentsBody: CommentsBody,
        @Query("page") page: Int
    ): Deferred<CommentsResponse>

    @GET("stores")
    fun getStoresAsync(@Query("page") page: Int): Deferred<StoresResponse>

    @GET("category/{categoryName}/{subCategoryName}")
    fun getAdsAuthAsync(
        @Header("Authorization") token: String,
        @Path("categoryName") categoryName: String,
        @Path("subCategoryName") subCategoryName: String,
        @Query("page") page: Int
    ): Deferred<AdsResponse>

    @GET("notifications")
    fun getNotisAsync(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): Deferred<NotificationsResponse>

    @GET("checkout/fawry")
    fun upgradeAccountAsync(
        @Header("Authorization") token: String
    ): Deferred<FavouriteActionResponse>

    @GET("checkout/upgradead/{id}")
    fun upgradeAdAsync(
        @Header("Authorization") token: String,
        @Path("id")id:String
    ): Deferred<FavouriteActionResponse>

    @GET("setnotfication")
    fun readNotisAsync(
        @Header("Authorization") token: String
    ): Deferred<FavouriteActionResponse>

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
        @Part avatar: MultipartBody.Part
    ): Deferred<UpdateProfileResponse>

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
        @Part avatar: MultipartBody.Part
    ): Deferred<UpdateProfileResponse>

    @Multipart
    @POST("createstore")
    fun addShopAsync(
        @Header("Authorization") token: String,
        @Part("username") username: RequestBody,
        @Part("title") title: RequestBody,
        @Part("short_desc") short_desc: RequestBody,
        @Part("long_desc") long_desc: RequestBody,
        @Part("category") category: Int,
        @Part logo: MultipartBody.Part,
        @Part cover: MultipartBody.Part
    ): Deferred<AddShopResponse>

    @POST("profile")
    fun updateProfileWithoutImageAsync(
        @Header("Authorization") token: String,
        @Body updateProfileBody: UpdateProfileBody
    ): Deferred<UpdateProfileResponse>

    @POST("reportad")
    fun reportAdAsync(
        @Header("Authorization") token: String,
        @Body reportAdBody: ReportAdBody
    ): Deferred<ReportAdResponse>


    @POST("setoffer")
    fun adOfferAsync(
        @Header("Authorization") token: String,
        @Body adOfferBody: AdOfferBody
    ): Deferred<AdOfferResponse>

    @POST("acceptdenyoffer")
    fun offersActionAsync(
        @Header("Authorization") token: String,
        @Body offersActionBody: OffersActionBody
    ): Deferred<OffersActionResponse>

    //===================== Static Pages ==================================
    @GET("page/aboutcezma")
    fun aboutUsPageAsync(): Deferred<PagesResponse>


    @GET("page/terms")
    fun termsPageAsync(): Deferred<PagesResponse>


    @GET("page/howtousecezma")
    fun howToUsePageAsync(): Deferred<PagesResponse>


    @GET("page/howtoopenshop")
    fun howToOpenPageAsync(): Deferred<PagesResponse>


    @GET("page/privacy")
    fun privacyPageAsync(): Deferred<PagesResponse>
}