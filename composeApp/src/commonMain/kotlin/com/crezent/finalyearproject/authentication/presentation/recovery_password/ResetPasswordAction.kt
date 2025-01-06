package com.crezent.finalyearproject.authentication.presentation.recovery_password

sealed interface ResetPasswordAction {

    data class EditPassword(val password: String) : ResetPasswordAction
    data class EditConfirmPassword(val password: String) : ResetPasswordAction

    data object ChangePassword : ResetPasswordAction
}