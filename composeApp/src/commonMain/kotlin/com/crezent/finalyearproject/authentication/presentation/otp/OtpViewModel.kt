package com.crezent.finalyearproject.authentication.presentation.otp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crezent.finalyearproject.VERIFY_EMAIL_PURPOSE
import com.crezent.finalyearproject.authentication.data.AuthenticationRepo
import com.crezent.finalyearproject.domain.util.RemoteError
import com.crezent.finalyearproject.domain.util.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class OtpViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val authenticationRepo: AuthenticationRepo
) : ViewModel() {
    private val _channel: Channel<OtpScreenEvent> = Channel()
    val channel = _channel.receiveAsFlow()

    private val emailAddress = savedStateHandle.get<String>("email")!!
    private val purpose = savedStateHandle.get<String>("purpose")!!

    private var timerJob: Job? = null
    private var delay = 120


    private val _otpScreenState = MutableStateFlow(
        OtpScreenState(emailAddress = emailAddress)
    )
    val otpScreenState = _otpScreenState.onStart {
        startTimer()
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            _otpScreenState.value
        )

    fun handleUserAction(action: OtpScreenAction) {
        when (action) {
            is OtpScreenAction.EditOtp -> editOtp(action.input)
            OtpScreenAction.Submit -> submit()
            OtpScreenAction.RequestNewOtp -> requestNewOtp()
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
            _otpScreenState.value = otpScreenState.value.copy(isLoading = true)
            delay(1000)

            val otpVerificationResult = authenticationRepo.verifyOtp(
                emailAddress = otpScreenState.value.emailAddress,
                purpose = purpose,
                otp = otpScreenState.value.otp
            )
            _otpScreenState.value = otpScreenState.value.copy(isLoading = false)

            if (otpVerificationResult is Result.Success) {
                handleOtpSuccessful()
                return@launch
            }
            val remoteError = otpVerificationResult as Result.Error
            _otpScreenState.value = otpScreenState.value.copy(wrongOtp = true)
            _channel.send(OtpScreenEvent.OtpScreenError(remoteError.error))
        }

    }

    private suspend fun handleOtpSuccessful() {
        if (purpose == VERIFY_EMAIL_PURPOSE) {
            verifyEmail()
            return
        }
        _channel.send(OtpScreenEvent.NavigateToResetPassword)

    }

    private suspend fun verifyEmail() {
        val emailVerification = authenticationRepo.verifyEmail()
        if (emailVerification is Result.Error) {
            _channel.send(OtpScreenEvent.OtpScreenError(emailVerification.error))
            if (emailVerification.error == RemoteError.UnAuthorised) {
                //TODO
            }
            return
        }
        _channel.send(OtpScreenEvent.NavigateToHome)


    }


    private fun requestNewOtp() {
        _otpScreenState.value =
            otpScreenState.value.copy(requestNewButtonEnable = true, isLoading = true)
        println("Email address requesting otp is $emailAddress")
        viewModelScope.launch {
            val result = authenticationRepo.requestOtp(
                emailAddress = emailAddress,
                purpose = purpose
            )
            _otpScreenState.value =
                otpScreenState.value.copy(isLoading = false, requestNewButtonEnable = false)
            startTimer()

            if (result is Result.Success) {
                _channel.send(OtpScreenEvent.OtpSentSuccessful)
            } else {
                val error = (result as Result.Error).error
                _channel.send(OtpScreenEvent.OtpScreenError(error = error))
            }

        }

    }

    private fun startTimer() {
        delay = 120
        timerJob?.cancel()
        timerJob = Job()
        CoroutineScope(Dispatchers.Main + timerJob!!).launch {
            while (delay > 0) {
                delay -= 1
                delay(1000)
                _otpScreenState.value = otpScreenState.value.copy(
                    remainingTime = delay.toTimer()
                )
            }
            _otpScreenState.value =
                otpScreenState.value.copy(requestNewButtonEnable = true, remainingTime = "")


        }

    }

}

private fun Int.toTimer(): String {
    val min = this / 60

    val sec = this % 60
    val minText = if (min > 9) "$min" else "0$min"
    val secText = if (sec > 9) "$sec" else "0$sec"
    return "$minText:$secText"
}
