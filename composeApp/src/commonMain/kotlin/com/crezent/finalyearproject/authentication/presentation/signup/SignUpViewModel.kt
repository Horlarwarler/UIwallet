package com.crezent.finalyearproject.authentication.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crezent.finalyearproject.authentication.data.AuthenticationRepo
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.domain.util.ValidationUtils
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class SignUpViewModel(
    private val authenticationRepo: AuthenticationRepo
) : ViewModel() {
    private val _channel: Channel<SignUpEvent> = Channel()
    val channel = _channel.receiveAsFlow()

    private val _signUpState = MutableStateFlow(SignUpScreenState())
    val signUpScreenState = _signUpState.asStateFlow()

    fun handleUserAction(action: SignUpAction) {
        when (action) {
            is SignUpAction.EditEmail -> editEmail(action.input)
            is SignUpAction.EditPassword -> editPassword(action.password)
            SignUpAction.SignUp -> signUp()
            is SignUpAction.EditConfirmPassword -> editConfirmPassword(action.confirmPassword)
            is SignUpAction.EditFullName -> editFullName(action.name)
            is SignUpAction.EditMatric -> editMatric(action.matric)
            is SignUpAction.EditPhoneNumber -> editPhoneNumber(action.phoneNumber)
            is SignUpAction.EditGender -> editGender(action.gender)
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

        _signUpState.value = signUpScreenState.value.copy(
            emailFieldError = emailError,
            email = email
        )

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

        _signUpState.value = signUpScreenState.value.copy(
            passwordFieldError = passwordFieldError,
            password = password
        )

    }

    private fun editConfirmPassword(confirmPassword: String) {
        val passwordMatched = confirmPassword == signUpScreenState.value.password

        val confirmPasswordFieldError = mutableListOf<String>()

        if (!passwordMatched) {
            confirmPasswordFieldError.add("Password does not match")
        }
        _signUpState.value = signUpScreenState.value.copy(
            confirmPassword = confirmPassword,
            confirmPasswordFieldError = confirmPasswordFieldError
        )
    }

    private fun editFullName(input: String) {
        val fullNameRegex = Regex("^[A-Za-z]+(?: [A-Za-z]+){1,2}\$")

        val matches = fullNameRegex.matches(input)

        _signUpState.value = signUpScreenState.value.copy(
            fullName = input,
            fullNameError = if (!matches) listOf("Name must be in format \"FName  LastName Other name\" ") else emptyList()
        )
    }


    private fun editMatric(input: String) {
        _signUpState.value = signUpScreenState.value.copy(
            matricNumber = input,
            matricNumberFieldError = if (input.isBlank()) listOf("Matric can't be blank") else emptyList()
        )
    }


    private fun editPhoneNumber(input: String) {

        val inputIsValid = ValidationUtils.isValidPhoneNumber(input)


        _signUpState.value = signUpScreenState.value.copy(
            phoneNumber = input,
            phoneNumberFieldError = if (inputIsValid) emptyList() else listOf("Number can't be blank")
        )
    }

    private fun editGender(gender: String) {
        _signUpState.value = signUpScreenState.value.copy(
            gender = gender,
        )
    }

    private fun signUp() {
        viewModelScope.launch {
            _signUpState.value = signUpScreenState.value.copy(isLoading = true)
            delay(1000)
            val result = authenticationRepo.signUp(
                emailAddress = signUpScreenState.value.email,
                password = signUpScreenState.value.password,
                fullName = signUpScreenState.value.fullName,
                gender = signUpScreenState.value.gender,
                phoneNumber = signUpScreenState.value.phoneNumber,
                matricNumber = signUpScreenState.value.matricNumber
            )
            _signUpState.value = signUpScreenState.value.copy(isLoading = false)

            println("Email is ${signUpScreenState.value.email}")
            if (result is Result.Success) {
                _channel.send(SignUpEvent.NavigateToOtp(email = signUpScreenState.value.email))
                return@launch
            }
            val remoteError = result as Result.Error

            _channel.send(SignUpEvent.ShowError(remoteError.error))

        }
    }
}