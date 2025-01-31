package com.crezent.finalyearproject.core.domain.preference

interface EncryptedSharePreference {

    val userPin: String?

    fun setUserPin(pin: String)

    val otpToken: String?

    fun saveOtpToken(token: String)

    fun deleteKey(key: String)

    val getAuthToken: String?

    fun editAuthToken(token: String)


    companion object {
        const val PIN = "pin"
        const val OTP_TOKEN = "otp_token"
        const val AUTH_TOKEN = "auth_token"
    }

}