package com.crezent.finalyearproject.authentication.presentation.recovery_password

data class ResetPasswordScreenState(
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val passwordFieldError: List<String> = emptyList(),
    val confirmPasswordFieldError: List<String> = emptyList(),

    )