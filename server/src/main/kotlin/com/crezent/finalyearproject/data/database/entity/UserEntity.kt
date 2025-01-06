package com.crezent.finalyearproject.data.database.entity

import com.crezent.finalyearproject.data.dto.LoggedInUser
import com.crezent.finalyearproject.models.Gender
import com.crezent.finalyearproject.models.User
import com.crezent.finalyearproject.models.Wallet
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class UserEntity(
    @BsonId
    val id: ObjectId,
    val userName: String,
    val matricNumber: String,
    val wallet: WalletEntity? = null,
    val gender: String,
    val phoneNumberString: String,
    val emailAddress: String,
    val emailAddressVerified: String,
    val firstName: String,
    val lastName: String,
    val middleName: String? = null,
    val hashedPassword: String,
    val lastUsedPasswords: List<String> = emptyList(),
    val accountStatus: String = "active",
    val accountDeactivationReason: String? = null

) {

    fun toUser(): User {
        return User(
            id = id.toString(),
            userName = userName,
            matricNumber = matricNumber,
            wallet = wallet?.toWallet(),
            gender = gender.toGender(),
            phoneNumberString = phoneNumberString,
            emailAddress = emailAddress,
            firstName = firstName,
            lastName = lastName,
            middleName = middleName,
            hashedPassword = hashedPassword,
            salt = "",
            lastUsedPasswords = lastUsedPasswords,
            accountActive = accountStatus == "active",
            accountDeactivationReason = accountDeactivationReason,
            emailAddressVerified = emailAddressVerified == "true",

            )
    }

    fun toResponseUser(): User {
        return User(
            id = id.toString(),
            userName = userName,
            matricNumber = matricNumber,
            wallet = null,
            gender = gender.toGender(),
            phoneNumberString = phoneNumberString,
            emailAddress = emailAddress,
            firstName = firstName,
            lastName = lastName,
            middleName = middleName,
            hashedPassword = null,
            salt = null,
            lastUsedPasswords = null,
            emailAddressVerified = emailAddressVerified == "true",

            accountActive = accountStatus == "active",
            accountDeactivationReason = accountDeactivationReason
        )
    }

    fun toLoggedInUser(): LoggedInUser {
        return LoggedInUser(
            id = id.toString(),
            matricNumber = matricNumber,
            phoneNumberString = phoneNumberString,
            emailAddress = emailAddress,
            firstName = firstName,
            lastName = lastName,
            middleName = middleName,
            emailAddressVerified = emailAddressVerified == "true",
            accountActive = accountStatus == "active",
            accountDeactivationReason = accountDeactivationReason
        )
    }

    private fun String.toGender(): Gender {
        return if (this == "M") Gender.Male else Gender.Female
    }
}



