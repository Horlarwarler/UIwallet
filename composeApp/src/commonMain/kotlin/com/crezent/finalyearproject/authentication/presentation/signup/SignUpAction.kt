package com.crezent.finalyearproject.authentication.presentation.signup

sealed interface SignUpAction {
    data class EditEmail(val input: String) : SignUpAction

    data class EditPassword(val password: String) : SignUpAction

    data object SignUp : SignUpAction

    data class EditFullName(val name: String) : SignUpAction

    data class EditMatric(val matric: String) : SignUpAction

    data class EditConfirmPassword(val confirmPassword: String) : SignUpAction

    data class EditPhoneNumber(val phoneNumber: String) : SignUpAction

    data class EditGender(val gender: String) : SignUpAction
}