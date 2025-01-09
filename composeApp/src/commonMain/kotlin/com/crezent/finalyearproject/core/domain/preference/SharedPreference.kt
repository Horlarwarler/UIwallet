package com.crezent.finalyearproject.core.domain.preference

interface SharedPreference {

    val showOnboarding: Boolean

    fun editShowOnboarding(value: Boolean)

    fun editPublicEcKey(apiKey :String)

    val publicEcKey :String?

    fun editPublicRsaKey(apiKey :String)

    val publicRsaKey :String?

    companion object {
        const val SHOW_ONBOARDING = "show_onboarding"
        const val PUBLIC_EC_KEY= "public_ec_key"
        const val PUBLIC_RSA_KEY= "public_rsa_key"

    }
}