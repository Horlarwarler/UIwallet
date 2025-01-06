package com.crezent.finalyearproject.authentication.presentation.signin

import com.crezent.finalyearproject.core.presentation.util.UiText

sealed interface SignInEvent {

    data object SignInSuccessful : SignInEvent

    data class SignInError(val error: UiText) : SignInEvent
}