package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmailRequest(
    val from: Recipient,
    val to: List<Recipient>,
    val subject: String,
    @SerialName("template_id")
    val templatedId: String,
    val personalization: List<CustomPersonalization>
) {
    @Serializable
    data class Recipient(
        val email: String,
        val name: String
    )

    @Serializable
    data class CustomPersonalization(
        val email: String,
        @SerialName("data")
        val personalizationData: PersonalizationData
    )

    @Serializable
    sealed interface PersonalizationData {
        @Serializable
        data class OtpData(
            @SerialName("support_email")
            val supportEmail: String,
            val otp: String
        ) : PersonalizationData
    }
}
