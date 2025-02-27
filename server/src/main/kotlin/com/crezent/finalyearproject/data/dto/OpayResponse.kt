package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class OpayResponse<T>(
    val code: String,
    val message: String,
    val data: T
)
