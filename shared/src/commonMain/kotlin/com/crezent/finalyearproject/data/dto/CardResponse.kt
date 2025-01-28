package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CardResponse(
    val id: String,
    val lastFourCharacter: String,
    val holderName: String
)
