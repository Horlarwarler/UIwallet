package com.crezent.finalyearproject.authentication.presentation.recovery_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crezent.finalyearproject.authentication.data.repo.AuthenticationRepoImpl
import com.crezent.finalyearproject.domain.util.RemoteError
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.domain.util.ValidationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class ResetPasswordScreenViewModel(
    private val authenticationRepoImpl: AuthenticationRepoImpl
) : ViewModel() {
    private val _channel: Channel<ResetPasswordEvent> = Channel()
    val channel = _channel.receiveAsFlow()

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
            _resetPasswordScreenState.value = recoveryScreenState.value.copy(isLoading = true)

            val result = authenticationRepoImpl.resetPassword(recoveryScreenState.value.password)

            _resetPasswordScreenState.value = recoveryScreenState.value.copy(isLoading = false)

            if (result is Result.Success) {
                _channel.send(ResetPasswordEvent.RecoverySuccessful)
                return@launch
            }
            val error = result as Result.Error

            _channel.send(ResetPasswordEvent.RecoveryError(error.error))

            if (error.error is RemoteError.UnAuthorised) {
                _channel.send(ResetPasswordEvent.UnAuthorised)
            }


        }

    }
}