package com.crezent.finalyearproject.core.domain.model


data class User(
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
    val wallet: Wallet?,
    val connectedCards: List<Card>
)
