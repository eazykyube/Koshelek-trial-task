package com.example.koshelek_trial_task

import com.squareup.moshi.Json

data class Ticker(
    @Json(name = "e")
    val eventType: String,
    @Json(name = "E")
    val eventTime: Int,
    @Json(name = "s")
    val symbol: String,
    @Json(name = "U")
    val firstId: Int,
    @Json(name = "u")
    val lastId: Int,
    @Json(name = "b")
    val bids: List<List<String>>,
    @Json(name = "a")
    val asks: List<List<String>>

)