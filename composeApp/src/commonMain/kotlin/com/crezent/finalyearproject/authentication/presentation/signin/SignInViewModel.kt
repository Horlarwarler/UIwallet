package com.crezent.finalyearproject.authentication.presentation.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crezent.finalyearproject.authentication.data.AuthenticationRepo
import com.crezent.finalyearproject.domain.util.ValidationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class SignInViewModel(
    private val authenticationRepo: AuthenticationRepo
) : ViewModel() {
    val channel: Channel<SignInEvent> = Channel()

    private val _signInState = MutableStateFlow(SignInScreenState())
    val signInScreenState = _signInState
        .onStart {
            getApiKey()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(500), _signInState.value)


    fun handleUserAction(action: SignInAction) {
        when (action) {
            is SignInAction.EditEmail -> editEmail(action.input)
            is SignInAction.EditPassword -> editPassword(action.password)
            SignInAction.Login -> login()
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
            emailError.add("Email can't contain error")
        }

        _signInState.value = signInScreenState.value.copy(
            emailFieldError = emailError,
            email = email
        )

    }

    private fun getApiKey() {
        viewModelScope.launch(Dispatchers.IO) {
            authenticationRepo.getApiKey()
        }
    }

    private fun editPassword(password: String) {
        val passwordValidation = ValidationUtils

        val passwordIsValid = passwordValidation.isPasswordStrong(password)

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

        _signInState.value = signInScreenState.value.copy(
            passwordFieldError = passwordFieldError,
            password = password
        )

    }

    private fun login() {
        viewModelScope.launch {
            authenticationRepo.login(
                emailAddress = signInScreenState.value.email,
                password = signInScreenState.value.password
            )
            // authenticationRemote.login(encryptedData = "Bla", "Hello", "hsddd")
        }
    }
}