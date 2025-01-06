package com.crezent.finalyearproject.authentication.presentation.signin

data class SignInScreenState(
    val email: String = "",
    val password: String = "",
    val emailFieldError: List<String> = emptyList(),
    val passwordFieldError: List<String> = emptyList(),

)