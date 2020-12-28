package com.example.koshelek_trial_task

import com.google.gson.annotations.SerializedName


data class Message(
    @SerializedName("e")
    val eventType: String,
    @SerializedName("E")
    val eventTime: Long,
    @SerializedName("s")
    val symbol: String,
    @SerializedName("U")
    val firstUpdate: Long,
    @SerializedName("u")
    val lastUpdate: Long,
    @SerializedName("b")
    val bids: List<List<String>>,
    @SerializedName("a")
    val asks: List<List<String>>
)