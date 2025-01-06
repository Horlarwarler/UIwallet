package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpDetails(
    @SerialName("email_address")
    val emailAddress: String,
    @SerialName("user_name")
    val userName: String,
    val password: String,
    @SerialName("matric_number")
    val matricNumber: String,
    val gender: String,
    @SerialName("phone_number")
    val phoneNumber: String,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("middle_name")
    val middleName: String? = null,
)
