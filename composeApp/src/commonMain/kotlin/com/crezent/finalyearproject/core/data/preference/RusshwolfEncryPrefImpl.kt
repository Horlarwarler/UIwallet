package com.crezent.finalyearproject.core.data.preference

import com.crezent.finalyearproject.core.domain.preference.EncryptedSharePreference
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

    override fun deleteKey(key: String) {
        settings.remove(key)
    }
}