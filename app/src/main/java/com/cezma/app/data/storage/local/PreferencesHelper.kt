package com.cezma.app.data.storage.local

import android.content.Context
import android.preference.PreferenceManager
import com.cezma.app.utiles.Constants

class PreferencesHelper(private val context: Context) {

    companion object {
        private const val IS_LOGGED_IN = "isLoggedIn"
        private const val TOKEN = "token"
        private const val REFRESH_TOKEN = "refreshToken"
        private const val ID = "id"
        private const val ID_VERIFIED = "id_verified"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val CITY_NAME = "cityName"
        private const val CITY_ID = "cityId"
        private const val MOBILE = "mobile"
        private const val GENDER = "gender"
        private const val LANGUAGE = "language"
    }

    private val preference = PreferenceManager.getDefaultSharedPreferences(context)

    var isLoggedIn = preference.getBoolean(IS_LOGGED_IN, false)
        set(value) = preference.edit().putBoolean(IS_LOGGED_IN, value).apply()

    var token = preference.getString(TOKEN, null)
        set(value) = preference.edit().putString(TOKEN, value).apply()

    var refreshToken = preference.getString(REFRESH_TOKEN, null)
        set(value) = preference.edit().putString(REFRESH_TOKEN, value).apply()

    var id = preference.getInt(ID, -1)
        set(value) = preference.edit().putInt(ID, value).apply()

    var id_verified = preference.getInt(ID_VERIFIED, 0)
        set(value) = preference.edit().putInt(ID_VERIFIED, value).apply()

    var name = preference.getString(NAME, null)
        set(value) = preference.edit().putString(NAME, value).apply()

    var email = preference.getString(EMAIL, null)
        set(value) = preference.edit().putString(EMAIL, value).apply()

    var cityId = preference.getInt(CITY_ID, -1)
        set(value) = preference.edit().putInt(CITY_ID, value).apply()

    var cityName = preference.getString(CITY_NAME, null)
        set(value) = preference.edit().putString(CITY_NAME, value).apply()

    var mobile = preference.getString(MOBILE, null)
        set(value) = preference.edit().putString(MOBILE, value).apply()

    var gender = preference.getString(GENDER, null)
        set(value) = preference.edit().putString(GENDER, value).apply()

    var language = preference.getString(LANGUAGE, Constants.Language.ARABIC.value)
        set(value) = preference.edit().putString(LANGUAGE, value).apply()

    fun clear() {
        val lang = language
        preference.edit().clear().apply()
        language = lang
    }
}