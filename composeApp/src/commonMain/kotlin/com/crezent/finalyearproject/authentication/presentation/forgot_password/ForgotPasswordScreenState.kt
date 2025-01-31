package com.crezent.finalyearproject.authentication.presentation.forgot_password

data class ForgotPasswordScreenState(
    val email: String = "",
    val emailFieldError: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val requestNewButtonEnable: Boolean = false,
    val remainingTime: String = "02:00"
)
