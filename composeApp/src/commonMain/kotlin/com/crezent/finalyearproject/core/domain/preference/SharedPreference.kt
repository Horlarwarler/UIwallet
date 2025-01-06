package com.crezent.finalyearproject.core.domain.preference

interface SharedPreference {

    val showOnboarding: Boolean

    fun editShowOnboarding(value: Boolean)

    fun editServerApiKey(apiKey :String)

    val serverApiKey :String?

    companion object {
        const val SHOW_ONBOARDING = "show_onboarding"
        const val SERVER_PUBLIC_KEY= "server_public_key"

    }
}