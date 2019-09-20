package com.cezma.app.utiles

import com.cezma.app.CezmaApp
import com.cezma.app.data.storage.local.PreferencesHelper
import com.cezma.app.data.storage.remote.RetrofitApiService
import com.cezma.app.repo.*
import com.cezma.app.utiles.Constants.BASE_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object Injector {

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private fun getApiServiceHeader(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("Accept", "application/json")

            if (chain.request().header("Accept-Language") == null) {
                request.addHeader(
                    "Accept-Language",
                    chain.request().header("Accept-Language") ?: getPreferenceHelper().language!!
                )
            }
            chain.proceed(request.build())
        }
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(getApiServiceHeader())
            .addInterceptor(getLoggingInterceptor())
            .build()
    }

    private fun create(client: OkHttpClient): RetrofitApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .client(client)
            .build()

        return retrofit.create(RetrofitApiService::class.java)
    }

    private fun getApiService() = create(getOkHttpClient())

    fun getApplicationContext() = CezmaApp.instance

    fun getPreferenceHelper() = PreferencesHelper(getApplicationContext())

    //==============================================================================================
    fun getCategoriesRepo() = CategoriesRepo(getApiService())

    fun getSubCategoriesRepo() = SubCategoriesRepo(getApiService())

    fun getAdsRepo() = AdsRepo(getApiService(), getPreferenceHelper())

    fun getLoginRepo() = LoginRepo(getApiService())

    fun getAdRepo() = AdRepo(getApiService(), getPreferenceHelper())

    fun getFavouritesAdsRepo() = FavouriteAdsRepo(getApiService(), getPreferenceHelper())

    fun getStoresRepo() = StoresRepo(getApiService())

    fun getStoreDetailsRepo() = StoreDetailsRepo(getApiService())

    fun getFavouriteActionRepo() = FavouriteActionRepo(getApiService(), getPreferenceHelper())

    fun getProfileRepo() = ProfileRepo(getApiService(), getPreferenceHelper())

    fun getCountriesRepo() = CountriesRepo(getApiService())

    fun getStatesRepo() = StatesRepo(getApiService())

    fun getCitiesRepo() = CitiesRepo(getApiService())

    fun getRefreshTokenRepo() = RefreshTokenRepo(getApiService(), getPreferenceHelper())

    fun getUpdateProfileRepo() = UpdateProfileRepo(getApiService(), getPreferenceHelper())

    fun getAdsByOwnerId() = AdsByOwnerRepo(getApiService(), getPreferenceHelper())

    fun getRegisterRepo() = RegisterRepo(getApiService())

    fun getContactUsRepo() = ContactUsRepo(getApiService())

    fun getStaticPagesRepo() = StaticPagesRepo(getApiService())

    fun getReportAdRepo() = ReportAdRepo(getApiService(), getPreferenceHelper())

    fun getAdOfferRepo() = AdOfferRepo(getApiService(), getPreferenceHelper())

    fun getLogoutRepo() = LogoutRepo(getApiService(), getPreferenceHelper())

    fun getOffersRepo() = OffersRepo(getApiService(), getPreferenceHelper())

    fun getOffersActionRepo() = OffersActionRepo(getApiService(), getPreferenceHelper())

    fun getWriteCommentRepo() = WriteCommentRepo(getApiService(), getPreferenceHelper())

    fun getVerifyUserRepo() = VerifyUserRepo(getApiService(), getPreferenceHelper())

    fun getNotisRepo() = NotiRepo(getApiService(), getPreferenceHelper())

    fun getUpgradeAccountRepo() = UpgradeAccountRepo(getApiService(), getPreferenceHelper())

    fun getCommentsRepo() = CommentsRepo(getApiService(), getPreferenceHelper())

    fun getHomeAdsRepo() = HomeAdsRepo(getApiService())

    fun getSlidersRepo() = SlidersRepo(getApiService())

}