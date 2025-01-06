package com.crezent.finalyearproject.core.data.preference

import com.crezent.finalyearproject.core.domain.preference.SharedPreference
import com.crezent.finalyearproject.core.domain.preference.SharedPreference.Companion.SERVER_PUBLIC_KEY
import com.crezent.finalyearproject.core.domain.preference.SharedPreference.Companion.SHOW_ONBOARDING
import com.russhwolf.settings.Settings


class RusshwolfSharedPrefImpl(
    private val settings: Settings
) : SharedPreference {
    override val showOnboarding: Boolean
        get() = settings.getBoolean(SHOW_ONBOARDING, true)

    override fun editShowOnboarding(value: Boolean) {
        settings.putBoolean(SHOW_ONBOARDING, value)
    }

    override fun editServerApiKey(apiKey: String) {
        settings.putString(SERVER_PUBLIC_KEY, apiKey)
    }

    override val serverApiKey: String?
        get() = settings.getStringOrNull(SERVER_PUBLIC_KEY)

}