package com.crezent.finalyearproject.authentication.presentation.signup

data class SignUpScreenState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailFieldError: List<String> = emptyList(),
    val confirmPasswordFieldError: List<String> = emptyList(),
    val passwordFieldError: List<String> = emptyList(),
    val phoneNumber: String = "",
    val phoneNumberFieldError: List<String> = emptyList(),
    val matricNumber: String = "",
    val matricNumberFieldError: List<String> = emptyList(),
    val gender: String = "M",
    val genderFieldError: List<String> = emptyList(),
    val fullName: String = "",
    val fullNameError: List<String> = emptyList(),
    val isLoading :Boolean = false
)
