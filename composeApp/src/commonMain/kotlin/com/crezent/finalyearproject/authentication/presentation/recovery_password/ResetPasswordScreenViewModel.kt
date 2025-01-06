package com.crezent.finalyearproject.authentication.presentation.recovery_password

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


class ResetPasswordScreenViewModel : ViewModel() {
    val channel: Channel<ResetPasswordEvent> = Channel()

    private val _resetPasswordScreenState = MutableStateFlow(ResetPasswordScreenState())
    val recoveryScreenState = _resetPasswordScreenState.asStateFlow()

    fun handleUserAction(action: ResetPasswordAction) {
        when (action) {
            is ResetPasswordAction.EditConfirmPassword -> editConfirmPassword(action.password)
            is ResetPasswordAction.EditPassword -> editPassword(action.password)
            ResetPasswordAction.ChangePassword -> changePassword()
        }
    }


    private fun editPassword(password: String) {
        val passwordValidation = ValidationUtils

        val passwordContainsDigit = passwordValidation.passwordContainsDigit(password)
        val passwordContainsCharacter = passwordValidation.fieldContainsSpecialCharacter(password)

        val passwordContainSpace = passwordValidation.fieldContainsSpace(password)

        val passwordContainsCapital = passwordValidation.passwordContainsCapital(password)

        val passwordFieldError = mutableListOf<String>()

        if (!passwordContainsCharacter) {
            passwordFieldError.add("Password is weak, must contain special character")
        }
        if (!passwordContainsDigit) {
            passwordFieldError.add("Password Must contain Number")
        }

        if (passwordContainSpace) {
            passwordFieldError.add("Password Must not contain space")
        }

        if (!passwordContainsCapital) {
            passwordFieldError.add("Password Must contain Upper case")

        }
        if (password.length < 8) {
            passwordFieldError.add("Password Must be greater than 8")
        }

        _resetPasswordScreenState.value = recoveryScreenState.value.copy(
            passwordFieldError = passwordFieldError,
            password = password
        )

    }

    private fun editConfirmPassword(confirmPassword: String) {
        val passwordMatched = confirmPassword == recoveryScreenState.value.password

        val confirmPasswordFieldError = mutableListOf<String>()

        if (!passwordMatched) {
            confirmPasswordFieldError.add("Password does not match")
        }
        _resetPasswordScreenState.value = recoveryScreenState.value.copy(
            confirmPassword = confirmPassword,
            confirmPasswordFieldError = confirmPasswordFieldError
        )
    }

    private fun changePassword() {

        viewModelScope.launch(Dispatchers.IO) {

            channel.send(ResetPasswordEvent.Loading)


//            _resetPasswordScreenState.value = recoveryScreenState.value.copy(isLoading = true)
//
            delay(3000)

            //Netwok request

            channel.send(ResetPasswordEvent.RecoverySuccessful)

            delay(3000)


        }

    }
}