package com.cezma.app.data.model

import com.squareup.moshi.Json

data class ReportAdResponse(
    @field:Json(name = "status")
    val status: Boolean,
    @field:Json(name = "msg")
    val msg: String
)

data class ReportAdBody(
    var ad_id: String
)