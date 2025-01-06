package com.crezent.finalyearproject.authentication.presentation.recovery_password

import com.crezent.finalyearproject.core.presentation.util.UiText

sealed interface ResetPasswordEvent {

    data object RecoverySuccessful : ResetPasswordEvent

    data class RecoveryError(val error: UiText) : ResetPasswordEvent

    data object Loading : ResetPasswordEvent
}