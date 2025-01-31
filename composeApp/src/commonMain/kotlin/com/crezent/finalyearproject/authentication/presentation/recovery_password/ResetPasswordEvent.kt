package com.crezent.finalyearproject.authentication.presentation.recovery_password

import com.crezent.finalyearproject.domain.util.RemoteError

sealed interface ResetPasswordEvent {

    data object RecoverySuccessful : ResetPasswordEvent

    data class RecoveryError(val error: RemoteError) : ResetPasswordEvent

    data object UnAuthorised : ResetPasswordEvent

}