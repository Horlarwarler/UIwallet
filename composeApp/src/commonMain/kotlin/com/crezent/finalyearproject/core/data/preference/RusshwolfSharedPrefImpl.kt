package com.crezent.finalyearproject.core.data.preference

import com.crezent.finalyearproject.core.domain.preference.SharedPreference
import com.crezent.finalyearproject.core.domain.preference.SharedPreference.Companion.LAST_BALANCE
import com.crezent.finalyearproject.core.domain.preference.SharedPreference.Companion.PUBLIC_EC_KEY
import com.crezent.finalyearproject.core.domain.preference.SharedPreference.Companion.PUBLIC_RSA_KEY
import com.crezent.finalyearproject.core.domain.preference.SharedPreference.Companion.SHOW_ONBOARDING
import com.crezent.finalyearproject.core.domain.preference.SharedPreference.Companion.USER_NAME
import com.russhwolf.settings.Settings


class RusshwolfSharedPrefImpl(
    private val settings: Settings
) : SharedPreference {
    override val showOnboarding: Boolean
        get() = settings.getBoolean(SHOW_ONBOARDING, true)

    override fun editShowOnboarding(value: Boolean) {
        settings.putBoolean(SHOW_ONBOARDING, value)
    }

    override fun editPublicEcKey(apiKey: String) {
        settings.putString(PUBLIC_EC_KEY, apiKey)
    }

    override val publicEcKey: String?
        get() = settings.getStringOrNull(PUBLIC_EC_KEY)

    override fun editPublicRsaKey(apiKey: String) {
        settings.putString(PUBLIC_RSA_KEY, apiKey)
    }

    override val publicRsaKey: String?
        get() = settings.getStringOrNull(PUBLIC_RSA_KEY)

    override fun saveUserFullName(name: String) {
        settings.putString(USER_NAME, name)
    }

    override fun updateBalance(balance: Double) {
        settings.putDouble(LAST_BALANCE, balance)
    }

    override val balance: Double?
        get() = settings.getDouble(LAST_BALANCE, 0.0)

    override val userFullName: String?
        get() = settings.getStringOrNull(USER_NAME)


}