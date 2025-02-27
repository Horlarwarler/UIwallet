package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.Serializable


@Serializable
data class PayStackResponse<T>(
    val status: Boolean,
    val message: String,
    val data: T
)

@Serializable
data class PayStackErrorResponse(
    val status: Boolean,
    val message: String,
)
