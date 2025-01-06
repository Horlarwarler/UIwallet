package com.crezent.finalyearproject.models

import kotlinx.serialization.Serializable


@Serializable
data class User(
    val id: String,
    val userName: String,
    val matricNumber: String,
    val wallet: Wallet?,
    val gender: Gender,
    val phoneNumberString: String,
    val emailAddress: String,
    val firstName: String,
    val lastName: String,
    val middleName: String? = null,
    val hashedPassword: String ?,
    val salt: String?,
    val lastUsedPasswords: List<String>? = emptyList(),
    val emailAddressVerified:Boolean ,
    val accountActive: Boolean,
    val accountDeactivationReason: String?
)


enum class Gender {
    Male,
    Female
}

enum class AccountStatus {
    Active,
    InActive,

}