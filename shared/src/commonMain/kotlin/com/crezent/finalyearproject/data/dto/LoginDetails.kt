package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginDetails(
    val emailAddress: String,
    val password: String
)