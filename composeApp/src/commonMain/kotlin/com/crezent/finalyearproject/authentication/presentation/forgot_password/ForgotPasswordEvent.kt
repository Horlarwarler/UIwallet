package com.crezent.finalyearproject.authentication.presentation.forgot_password

import com.crezent.finalyearproject.core.presentation.util.UiText

sealed interface ForgotPasswordEvent {
    data object OtpRequested : ForgotPasswordEvent

    data class SendMessage(val uiText: UiText) : ForgotPasswordEvent
}