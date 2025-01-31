package com.crezent.finalyearproject.authentication.presentation.otp

import com.crezent.finalyearproject.domain.util.RemoteError

sealed interface OtpScreenEvent {

    data object OtpScreenSuccessful : OtpScreenEvent
    data object OtpSentSuccessful : OtpScreenEvent

    data object NavigateToHome: OtpScreenEvent

    data object NavigateToResetPassword :OtpScreenEvent

    data class OtpScreenError(val error: RemoteError) : OtpScreenEvent
}