package com.crezent.finalyearproject.authentication.presentation.otp

sealed interface OtpScreenAction {
    data class EditOtp(val input: String) : OtpScreenAction


    data object Submit : OtpScreenAction
}