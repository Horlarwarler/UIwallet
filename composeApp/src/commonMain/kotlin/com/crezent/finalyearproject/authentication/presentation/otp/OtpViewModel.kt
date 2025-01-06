package com.crezent.finalyearproject.authentication.presentation.otp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class OtpViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val channel: Channel<OtpScreenEvent> = Channel()
    val emailAddress = savedStateHandle.get<String>("email") ?: "Guest"


    private val _otpScreenState = MutableStateFlow(
        OtpScreenState(recoveredEmail = emailAddress)
    )
    val otpScreenState = _otpScreenState.asStateFlow()

    fun handleUserAction(action: OtpScreenAction) {
        when (action) {
            is OtpScreenAction.EditOtp -> editOtp(action.input)
            OtpScreenAction.Submit -> submit()
        }
    }

    private fun editOtp(otp: String) {

        if (otp.length <= 6) {
            _otpScreenState.value = otpScreenState.value.copy(
                otp = otp,
                wrongOtp = false
            )
        }


    }


    private fun submit() {
        viewModelScope.launch {
            delay(3000)
            channel.send(OtpScreenEvent.OtpScreenSuccessful)
        }
    }
}