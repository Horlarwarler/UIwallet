package com.crezent.finalyearproject.authentication.presentation.signin

sealed interface SignInAction {
    data class EditEmail(val input: String) : SignInAction

    data class EditPassword(val password: String) : SignInAction

    data object Login : SignInAction
}