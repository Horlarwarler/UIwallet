package com.crezent.finalyearproject.splash.presesentation

import com.crezent.finalyearproject.core.presentation.util.UiText

sealed interface SplashScreenEvent {
    data object NavigateToOnboard : SplashScreenEvent

    data object NavigateToPinScreen : SplashScreenEvent

    data object NavigateToSignInScreen : SplashScreenEvent

    data class SendErrorMessage(val uiText: UiText) : SplashScreenEvent
}