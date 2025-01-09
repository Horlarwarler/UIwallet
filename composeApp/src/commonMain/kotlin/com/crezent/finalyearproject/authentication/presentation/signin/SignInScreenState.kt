package com.crezent.finalyearproject.authentication.presentation.signin

data class SignInScreenState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false

)