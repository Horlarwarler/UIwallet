package com.crezent.finalyearproject.authentication.presentation.forgot_password

sealed interface ForgotPasswordAction {
    data object SubmitOtp : ForgotPasswordAction

    data class EditEmail(val email: String) : ForgotPasswordAction

  //  data object RequestNewOtp : ForgotPasswordAction


}