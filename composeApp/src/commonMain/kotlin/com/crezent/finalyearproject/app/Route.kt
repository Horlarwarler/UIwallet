package com.crezent.finalyearproject.app

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    @Serializable
    data object SplashRoute : Route

    @Serializable
    data object OnboardRoute : Route

    @Serializable
    data object SignInRoute : Route

    @Serializable
    data object PinRoute : Route

    @Serializable
    data object SignUpRoute : Route

    @Serializable
    data object ForgotPassword : Route

    @Serializable
    data class OtpRoute(
        @SerialName("email")
        val emailAddress: String
    ) : Route


    @Serializable
    data object AuthenticationGraph : Route

    @Serializable
    data object ResetPassword : Route


}