package com.example.koshelek_trial_task

data class Subscribe(
    val method: String = "SUBSCRIBE",
    val params: List<String>,
    val id: Int
)