package com.crezent.finalyearproject.authentication.presentation.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crezent.finalyearproject.authentication.data.AuthenticationRepo
import com.crezent.finalyearproject.core.domain.BaseAppRepo
import com.crezent.finalyearproject.core.presentation.util.SnackBarController
import com.crezent.finalyearproject.core.presentation.util.SnackBarEvent
import com.crezent.finalyearproject.domain.util.RemoteError
import com.crezent.finalyearproject.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class SignInViewModel(
    private val authenticationRepo: AuthenticationRepo,
    private val baseAppRepo: BaseAppRepo

) : ViewModel() {
    private val _channel: Channel<SignInEvent> = Channel()

    val channel = _channel.receiveAsFlow()


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

        _signInState.value = signInScreenState.value.copy(
            email = email
        )

    }

    private fun getApiKey() {
        viewModelScope.launch(Dispatchers.IO) {
            baseAppRepo.getApiKey()
        }
    }

    private fun editPassword(password: String) {

        _signInState.value = signInScreenState.value.copy(
            password = password
        )

    }

    private fun login() {
        viewModelScope.launch {
            try {
                SnackBarController.sendEvent(SnackBarEvent.DismissSnackBar)
                _signInState.value = signInScreenState.value.copy(
                    isLoading = true
                )
                delay(1000)
                val loginResult = authenticationRepo.login(
                    emailAddress = signInScreenState.value.email,
                    password = signInScreenState.value.password
                )

                if (loginResult is Result.Error) {
                    _channel.send(SignInEvent.SignInError(error = loginResult.error))
                    return@launch
                }
//                val deactivationReason = loggedInResult.data.accountDeactivationReason
//                val accountDisable = loggedInResult.data.accountDeactivationReason != null
//
//                if (accountDisable) {
//                    _channel.send(SignInEvent.AccountDisable(deactivationReason!!))
//                    return@launch
//                }
//
//                val emailVerified = loggedInResult.data.emailAddressVerified
//                if (!emailVerified) {
//                    sendOtp(
//                        emailAddress = loggedInResult.data.emailAddress,
//                        userId = loggedInResult.data.id
//                    )
//                    return@launch
//                }
                _channel.send(SignInEvent.SignInSuccessful)
            } catch (e: Error) {
                _channel.send(
                    SignInEvent.SignInError(
                        error = RemoteError.UnKnownError(
                            e.message ?: ""
                        )
                    )
                )
            } finally {
                _signInState.value = signInScreenState.value.copy(isLoading = false)
            }

        }
    }

    private fun sendOtp(
        emailAddress: String,
        userId: String
    ) {
        viewModelScope.launch {
            val requestResult = authenticationRepo.requestOtp(
                emailAddress = emailAddress,
                purpose = "Verification"
            )
            if (requestResult is Result.Error) {
                _channel.send(SignInEvent.SignInError(requestResult.error))
                return@launch
            }
            _channel.send(SignInEvent.VerifyEmail(email = emailAddress, userId = userId))

        }
    }
}