package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResetPassword(
    val password: String
)
