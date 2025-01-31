package com.crezent.finalyearproject.authentication.presentation.signin

import com.crezent.finalyearproject.domain.util.RemoteError

sealed interface SignInEvent {

    data object SignInSuccessful : SignInEvent

    data class SignInError(val error: RemoteError) : SignInEvent

    data class VerifyEmail(val email: String, val userId:String) : SignInEvent

    data class AccountDisable(val reason: String) : SignInEvent

}