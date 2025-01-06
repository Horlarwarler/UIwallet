package com.crezent.finalyearproject.authentication.presentation.forgot_password

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crezent.finalyearproject.domain.util.ValidationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val channel: Channel<ForgotPasswordEvent> = Channel()

    private val _forgotPasswordState = MutableStateFlow(ForgotPasswordScreenState())
    val forgotPasswordScreenState = _forgotPasswordState.asStateFlow()

    fun handleUserAction(action: ForgotPasswordAction) {
        when (action) {
            is ForgotPasswordAction.EditEmail -> editEmail(action.email)
            ForgotPasswordAction.RequestOtp -> requestOtp()
        }
    }

    private fun editEmail(email: String) {
        val emailValidation = ValidationUtils
        val emailIsValid = emailValidation.isValidEmail(email)
        val emailContainSpace = emailValidation.fieldContainsSpace(email)

        val emailError: MutableList<String> = mutableListOf()

        if (!emailIsValid) {
            emailError.add("Email Is not valid")
        }
        if (emailContainSpace) {
            emailError.add("Email can't contain Space")
        }

        _forgotPasswordState.value = forgotPasswordScreenState.value.copy(
            emailFieldError = emailError,
            email = email
        )

    }


    private fun requestOtp() {

        viewModelScope.launch(Dispatchers.IO) {
            delay(2000)
            channel.send(ForgotPasswordEvent.OtpRequested)
        }
    }
}