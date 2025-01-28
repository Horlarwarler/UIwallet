package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CvvVerification(
    val cvv: String,
    val cardId:String
)
