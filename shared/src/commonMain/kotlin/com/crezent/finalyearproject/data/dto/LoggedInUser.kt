package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoggedInUser(
    val id: String,
    val matricNumber: String,
    val phoneNumberString: String,
    val emailAddress: String,
    val firstName: String,
    val lastName: String,
    val middleName: String? = null,
    val emailAddressVerified: Boolean,
    val accountActive: Boolean,
    val accountDeactivationReason: String?,
    val walletDto: WalletDto?,
    val connectedCards: List<CardResponse>
)
