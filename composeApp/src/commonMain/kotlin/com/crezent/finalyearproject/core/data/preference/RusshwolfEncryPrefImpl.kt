package com.crezent.finalyearproject.core.data.preference

import com.crezent.finalyearproject.core.domain.preference.EncryptedSharePreference
import com.crezent.finalyearproject.core.domain.preference.EncryptedSharePreference.Companion.AUTH_TOKEN
import com.crezent.finalyearproject.core.domain.preference.EncryptedSharePreference.Companion.OTP_TOKEN
import com.crezent.finalyearproject.core.domain.preference.EncryptedSharePreference.Companion.PIN
import com.russhwolf.settings.Settings

class RusshwolfEncryPrefImpl(
    private val settings: Settings

) : EncryptedSharePreference {
    override val userPin: String?
        get() = settings.getStringOrNull(PIN)

    override fun setUserPin(pin: String) {
        settings.putString(PIN, pin)
    }

    override val otpToken: String?
        get() = settings.getStringOrNull(OTP_TOKEN)

    override fun saveOtpToken(token: String) {
        settings.putString(OTP_TOKEN, token)
    }

    override fun deleteKey(key: String) {
        settings.remove(key)
    }

    override fun editAuthToken(token: String) {
        settings.putString(AUTH_TOKEN,token)
    }

    override val getAuthToken: String?
        get() = settings.getStringOrNull(AUTH_TOKEN, )


}