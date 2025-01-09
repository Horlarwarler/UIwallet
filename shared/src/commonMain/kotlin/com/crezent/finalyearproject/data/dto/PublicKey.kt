package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PublicKey(
    val publicEcKey: String,
    val publicRsaKey: String
)
