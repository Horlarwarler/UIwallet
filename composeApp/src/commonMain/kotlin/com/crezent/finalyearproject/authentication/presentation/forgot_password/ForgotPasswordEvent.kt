package com.crezent.finalyearproject.authentication.presentation.forgot_password

import com.crezent.finalyearproject.core.presentation.util.UiText
import com.crezent.finalyearproject.domain.util.RemoteError

sealed interface ForgotPasswordEvent {
    data object OtpRequested : ForgotPasswordEvent

    data class SendMessage(val networkError:RemoteError) : ForgotPasswordEvent
}