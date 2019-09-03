package com.cezma.app.data.model


import com.squareup.moshi.Json

data class FavoritesAdsResponse(
    @field:Json(name = "favorites")
    val favoriteAds: List<FavoriteAd>
)