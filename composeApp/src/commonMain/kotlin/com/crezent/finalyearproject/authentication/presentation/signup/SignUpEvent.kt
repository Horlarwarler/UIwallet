package com.crezent.finalyearproject.authentication.presentation.signup

import com.crezent.finalyearproject.domain.util.RemoteError

interface SignUpEvent {

    data class NavigateToOtp(val email: String) : SignUpEvent

    data class ShowError(val networkError: RemoteError) :SignUpEvent
}