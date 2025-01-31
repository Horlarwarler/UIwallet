package com.crezent.finalyearproject.authentication.presentation.forgot_password

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crezent.finalyearproject.authentication.data.AuthenticationRepo
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.domain.util.ValidationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val authenticationRepo: AuthenticationRepo
) : ViewModel() {

    private val _channel: Channel<ForgotPasswordEvent> = Channel()
    val channel = _channel.receiveAsFlow()


    private val _forgotPasswordState = MutableStateFlow(ForgotPasswordScreenState())
    val forgotPasswordScreenState = _forgotPasswordState.asStateFlow()

    fun handleUserAction(action: ForgotPasswordAction) {
        when (action) {
            is ForgotPasswordAction.EditEmail -> editEmail(action.email)
            ForgotPasswordAction.SubmitOtp -> submitOtp()
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


    private fun submitOtp() {

        viewModelScope.launch(Dispatchers.IO) {
            _forgotPasswordState.value = forgotPasswordScreenState.value.copy(isLoading = true)
            delay(1000)
            val result = authenticationRepo.requestOtp(
                emailAddress = forgotPasswordScreenState.value.email,
                purpose = "Reset Password"
            )
            _forgotPasswordState.value = forgotPasswordScreenState.value.copy(isLoading = false)

            if (result is Result.Error) {
                _channel.send(ForgotPasswordEvent.SendMessage(result.error))
                return@launch
            }
            _channel.send(ForgotPasswordEvent.OtpRequested)
        }
    }


}

