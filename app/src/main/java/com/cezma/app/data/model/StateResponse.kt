package com.cezma.app.data.model


import com.squareup.moshi.Json

data class StateResponse(
    @field:Json(name = "data")
    val states: List<StateModel>
)