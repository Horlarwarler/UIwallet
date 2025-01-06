package com.crezent.finalyearproject.authentication.presentation.otp

import com.crezent.finalyearproject.core.presentation.util.UiText

sealed interface OtpScreenEvent {

    data object OtpScreenSuccessful : OtpScreenEvent

    data class OtpScreenError(val error: UiText) : OtpScreenEvent
}