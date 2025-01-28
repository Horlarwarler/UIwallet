package com.crezent.finalyearproject.data.database.entity

import com.crezent.finalyearproject.data.dto.LoggedInUser
import com.crezent.finalyearproject.models.Gender
import com.crezent.finalyearproject.models.User
import com.crezent.finalyearproject.utility.security.encryption.EncryptService
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
    val accountDeactivationReason: String? = null,
    val connectedCards: List<CardEntity> = emptyList(),

    ) {

    fun toUser(): User {
        return User(
            id = id.toString(),
            userName = userName,
            matricNumber = matricNumber,
            walletDto = wallet?.toWallet(),
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
            walletDto = null,
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

    fun toLoggedInUser(
        encryptService: EncryptService,
        key: String
    ): LoggedInUser {
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
            accountDeactivationReason = accountDeactivationReason,
            connectedCards = connectedCards.map {
                it.toCardResponse(
                    encryptService = encryptService,
                    key = key,
                )
            },
            walletDto = wallet?.toWallet()
        )
    }

    private fun String.toGender(): Gender {
        return if (this == "M") Gender.Male else Gender.Female
    }
}



