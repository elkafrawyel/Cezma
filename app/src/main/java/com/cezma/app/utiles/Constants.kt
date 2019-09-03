package com.cezma.app.utiles

object Constants {
    const val BASE_URL = "https://cezma.com/mydtest/api/"
    const val AUTHORIZATION_START = "Bearer"

    const val howToOpenYourShopUrl = "http://r-z.store/privacyar.php"
    const val privacyUrl = "http://r-z.store/privacyar.php"
    const val TermsUrl = "http://r-z.store/privacyar.php"
    const val aboutUsUrl = "http://r-z.store/privacyar.php"



    enum class Language(val value: String) {
        ARABIC("ar"),
        ENGLISH("en")
    }
}